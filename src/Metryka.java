import java.util.*;

/**
 * Created by Mateusz on 2016-05-12.
 */
public class Metryka {

    double Euklidesowa(ArrayList<Deskryptor> tst,ArrayList<Deskryptor> trn)
    {
        double wynik=0;
        for (int i = 0; i <tst.size(); i++) {
            wynik+=Math.pow(tst.get(i).wartosc-trn.get(i).wartosc,2);
        }
        return Math.sqrt(wynik);
    }

    double Manhattan(ArrayList<Deskryptor> tst, ArrayList<Deskryptor> trn)
    {
        double wynik=0;
        for (int i = 0; i <tst.size() ; i++) {
            wynik=Math.abs(tst.get(i).wartosc-trn.get(i).wartosc);
        }
        return wynik;
    }

    double Canberra(ArrayList<Deskryptor> tst,ArrayList<Deskryptor> trn)
    {
        double wynik=0;
        for (int i = 0; i < tst.size(); i++) {
            wynik+=Math.abs((tst.get(i).wartosc-trn.get(i).wartosc)/(tst.get(i).wartosc+trn.get(i).wartosc));
        }
        return wynik;
    }

    double Czebyszewa(ArrayList<Deskryptor> tst, ArrayList<Deskryptor>trn)
    {
        double wynik=0;
        double tmp=0;
        for (int i = 0; i < tst.size(); i++) {
            tmp=Math.abs(tst.get(i).wartosc-trn.get(i).wartosc);
            if(wynik<tmp){
                wynik=tmp;
            }
        }
        return wynik;
    }

    double Pearsona(ArrayList<Deskryptor> tst, ArrayList<Deskryptor> trn)
    {
        double wynik=0;
        double sredniaTst=sredniaListy(tst);
        double sredniaTrn=sredniaListy(trn);
        for(int i=0;i<tst.size();i++){
            wynik+=((tst.get(i).wartosc-sredniaTst)/PearsonaLMianownik(tst,sredniaTst))
                * ((trn.get(i).wartosc-sredniaTrn)/PearsonaLMianownik(trn,sredniaTrn));
        }

        return 1-wynik/tst.size();
    }

    double PearsonaLMianownik(ArrayList<Deskryptor> lista, double sredniaListy){
        double wynik=0;
        for (Deskryptor d:lista) {
            wynik+= Math.pow(d.wartosc + sredniaListy(lista),2);
        }
        return Math.sqrt(wynik/lista.size());
    }

    double sredniaListy(ArrayList<Deskryptor> lista)
    {
        double suma=0;
        for (Deskryptor d:lista)
        {
            suma+=d.wartosc;
        }
        return suma/lista.size();
    }

    ArrayList<Sasiad> obliczWynikMetryki(ObiektDecyzyjny obiektTst,SystemDecyzyjny trn, String metryka, int k)
    {
        ArrayList<Sasiad> listaWynikow=new ArrayList<>();
        double wynikMetryki;
        switch(metryka){
            case "Euklidesowa":

                for (ObiektDecyzyjny obiektTRN:trn.obiekty) {
                        wynikMetryki=Euklidesowa(obiektTst.deskryptory,obiektTRN.deskryptory);
                        listaWynikow.add(new Sasiad(wynikMetryki,obiektTRN.decyzja));
                }
                break;
            case "Czebyszewa":

                for (ObiektDecyzyjny obiektTRN:trn.obiekty) {
                    wynikMetryki=(Czebyszewa(obiektTst.deskryptory,obiektTRN.deskryptory));
                    listaWynikow.add(new Sasiad(wynikMetryki,obiektTRN.decyzja));
                }
                break;
            case "Manhattan":

                for (ObiektDecyzyjny obiektTRN:trn.obiekty) {
                    wynikMetryki=(Manhattan(obiektTst.deskryptory,obiektTRN.deskryptory));
                    listaWynikow.add(new Sasiad(wynikMetryki,obiektTRN.decyzja));
                }
                break;
            case "Canberra":
                for (ObiektDecyzyjny obiektTRN:trn.obiekty) {
                    wynikMetryki=(Canberra(obiektTst.deskryptory,obiektTRN.deskryptory));
                    listaWynikow.add(new Sasiad(wynikMetryki,obiektTRN.decyzja));
                }
                break;
            case "Pearsona":

                for (ObiektDecyzyjny obiektTRN:trn.obiekty) {
                    wynikMetryki=(Pearsona(obiektTst.deskryptory,obiektTRN.deskryptory));
                    listaWynikow.add(new Sasiad(wynikMetryki,obiektTRN.decyzja));
                }
                break;
        }
        return listaWynikow;
    }

    Integer klasyfikujObiekt(ArrayList<Sasiad> listaWynikow, ArrayList<Integer> listaDecyzji,int k) {
        ArrayList<Sasiad> listaKNN;
        TreeMap<Double,Integer> listaWynikowIDecyzji=new TreeMap<>();
        double rownaSuma=0;
        for (int decyzja:listaDecyzji) {
            ArrayList<Sasiad> listaWynikowDlaDecyzji=new ArrayList<>();
            for (Sasiad wynik:listaWynikow) {
                if(wynik.klasaDecyzyjna==decyzja){
                    listaWynikowDlaDecyzji.add(wynik);
                }
            }
            Collections.sort(listaWynikowDlaDecyzji, new Comparator<Sasiad>() {
                @Override
                public int compare(Sasiad o1, Sasiad o2) {
                    return Double.compare(o1.odleglosc,o2.odleglosc);
                }
            });
            listaKNN=new ArrayList<Sasiad>(listaWynikowDlaDecyzji.subList(0,k));
            double sumaWynikow=0;
            for (Sasiad sasiad:listaKNN) {
                sumaWynikow+=sasiad.wynikKlasyfikacji;
            }
            if(listaWynikowIDecyzji.isEmpty()){
                listaWynikowIDecyzji.put(sumaWynikow,decyzja);
            }
            else if(listaWynikowIDecyzji.firstKey()==sumaWynikow){
                rownaSuma=sumaWynikow;
            }
            else{
                listaWynikowIDecyzji.put(sumaWynikow,decyzja);
            }
        }
        if(listaWynikowIDecyzji.firstKey()==rownaSuma){
            return null;
        }
        return listaWynikowIDecyzji.firstEntry().getValue();
    }

    String klasyfikujSystem(SystemDecyzyjny tst,SystemDecyzyjny trn,String metryka, int k){
        ArrayList<Integer> listaDecyzjiSysTrn=stworzListeDecyzji(trn);
        double acc;
        double cov;
        double liczbaChwyconychWKlasie;
        double liczbaObiektowKlasy;
        double tpr;
        HashMap<Integer,MacierzPredykcji> listaMp=new HashMap<>();
        Klasyfikacja sklasyfikowanyObiekt;
        ArrayList<Klasyfikacja> listaSklasyfikowanych=new ArrayList<>();
        MacierzPredykcji mp;
        for (ObiektDecyzyjny obiektTst:tst.obiekty) {
            sklasyfikowanyObiekt=new Klasyfikacja();
            sklasyfikowanyObiekt.decyzja=klasyfikujObiekt(obliczWynikMetryki(obiektTst,trn,metryka,k),listaDecyzjiSysTrn,k);
            if(!(sklasyfikowanyObiekt.decyzja==null)) {
                sklasyfikowanyObiekt.czyChwycony = true;
                if (sklasyfikowanyObiekt.decyzja == obiektTst.decyzja) {
                    sklasyfikowanyObiekt.czyPoprawny = true;
                }
            }
            listaSklasyfikowanych.add(sklasyfikowanyObiekt);

            }
        for(Integer decyzja:listaDecyzjiSysTrn){
            mp=new MacierzPredykcji(listaSklasyfikowanych,tst,decyzja);
            listaMp.put(decyzja,mp);
        }

        return generujRaport(listaMp,listaDecyzjiSysTrn);
    }

    String generujRaport(HashMap<Integer,MacierzPredykcji> listaMp, ArrayList<Integer> listaDecyzji){
        String raport;
        int d1=listaDecyzji.get(0);
        int d2=listaDecyzji.get(1);
        raport= "==============================================================\n";
        raport+="           "+d1+"      "+d2+" ";
        raport+="   No. of obj.    Accuracy    Coverage\n";
        raport+=d1+"           "+listaMp.get(d1).listaPrzypisan[0]+"    "+listaMp.get(d1).listaPrzypisan[1]+"       ";
        raport+=listaMp.get(d1).NofObj+"            "+listaMp.get(d1).accuracy+"            "+listaMp.get(d1).coverage+"\n";
        raport+=d2+"           "+listaMp.get(d2).listaPrzypisan[1]+"    "+listaMp.get(d2).listaPrzypisan[0]+"       ";
        raport+=listaMp.get(d2).NofObj+"            "+listaMp.get(d2).accuracy+"            "+listaMp.get(d2).coverage+"\n";
        raport+="-------------------------------------------------------------\n";
        raport+="TPR    "+listaMp.get(d1).tpr+"  "+listaMp.get(d2).tpr;
        return raport;
    }

    static ArrayList<Integer> stworzListeDecyzji(SystemDecyzyjny trn){
        ArrayList<Integer> listaDecyzji=new ArrayList<>();
        for(ObiektDecyzyjny obiektTrn:trn.obiekty){
            if(!listaDecyzji.contains(obiektTrn.decyzja)){
                listaDecyzji.add(obiektTrn.decyzja);
            }
        }
        return listaDecyzji;
    }

    static int obliczMaxK(SystemDecyzyjny trn){
        HashMap<Integer,Integer> licznoscDecyzji=new HashMap<>();
        Integer maxK;
        for (ObiektDecyzyjny o:trn.obiekty) {
            if(licznoscDecyzji.containsKey(o.decyzja)){
                licznoscDecyzji.put(o.decyzja,licznoscDecyzji.get(o.decyzja)+1);
            }
            else{
                licznoscDecyzji.put(o.decyzja,1);
            }
        }
        maxK=Collections.min(licznoscDecyzji.values());
        return maxK;
    }
}
