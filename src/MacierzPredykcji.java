import java.util.ArrayList;

/**
 * Created by Mateusz on 2016-06-02.
 */
public class MacierzPredykcji {
    Double accuracy;
    Double coverage;
    Double tpr;
    int[] listaPrzypisan;
    double NofObj;

    public MacierzPredykcji(ArrayList<Klasyfikacja> klasyfikacja, SystemDecyzyjny tst, Integer decyzja){
        accuracy=obliczAccuracy(klasyfikacja,tst,decyzja);
        coverage=obliczCoverage(klasyfikacja,tst,decyzja);
        tpr=obliczTPR(klasyfikacja,tst,decyzja);
        listaPrzypisan=generujListePrzypisan(klasyfikacja,tst,decyzja);
        NofObj=obliczLiczbeObiektow(tst,decyzja);
    }

    int[] generujListePrzypisan(ArrayList<Klasyfikacja> klasyfikacja,SystemDecyzyjny tst,Integer decyzja){
        int[] listaPrzypisan=new int[2];
        ArrayList<Integer> listaDecyzji=Metryka.stworzListeDecyzji(tst);

        for(int i=0;i<klasyfikacja.size();i++){
            if(klasyfikacja.get(i).decyzja==decyzja && klasyfikacja.get(i).czyPoprawny) {
                listaPrzypisan[0]++;
            }
            else if(klasyfikacja.get(i).czyChwycony && klasyfikacja.get(i).decyzja!=decyzja && !klasyfikacja.get(i).czyPoprawny){
                listaPrzypisan[1]++;
            }
        }



        return listaPrzypisan;
    }
    double obliczLiczbeChwyconych(SystemDecyzyjny tst, int decyzja, ArrayList<Klasyfikacja> klasyfikacja){
        double liczbaObiektow=0;
        for(int i=0;i<tst.obiekty.size();i++){
            if(tst.obiekty.get(i).decyzja==decyzja && klasyfikacja.get(i).czyChwycony){
                liczbaObiektow++;
            }

        }
        return liczbaObiektow;
    }
    double obliczLiczbeObiektow(SystemDecyzyjny tst, int decyzja){
        double liczbaObiektow=0;

        for (ObiektDecyzyjny obiekt:tst.obiekty){
            if(obiekt.decyzja==decyzja){
                liczbaObiektow++;
            }
        }
        return liczbaObiektow;
    }
    double obliczAccuracy(ArrayList<Klasyfikacja> klasyfikacja, SystemDecyzyjny tst, Integer decyzja){
        double liczbaPoprawnych=0;
        double liczbaChwyconych=obliczLiczbeChwyconych(tst,decyzja,klasyfikacja);


        for (Klasyfikacja obiekt:klasyfikacja) {
            if(obiekt.czyPoprawny && obiekt.decyzja==decyzja){
                liczbaPoprawnych++;
            }
        }
        return liczbaPoprawnych/liczbaChwyconych;
    }
    double obliczCoverage(ArrayList<Klasyfikacja> klasyfikacja,SystemDecyzyjny tst, Integer decyzja){
        double liczbaChwyconych=obliczLiczbeChwyconych(tst,decyzja,klasyfikacja);
        double liczbaObiektow=obliczLiczbeObiektow(tst,decyzja);
        return liczbaChwyconych/liczbaObiektow;
    }

    Double obliczTPR(ArrayList<Klasyfikacja> klasyfikacja, SystemDecyzyjny tst,Integer decyzja){
        double liczbaPoprawnych=0;
        double liczbaBlednych=0;
        Double tpr;

        for (Klasyfikacja obiekt:klasyfikacja) {
            if(obiekt.czyPoprawny && obiekt.decyzja==decyzja){
                liczbaPoprawnych++;
            }
            else if(!obiekt.czyPoprawny && obiekt.decyzja==decyzja){
                liczbaBlednych++;
            }
        }
        tpr=liczbaPoprawnych/(liczbaPoprawnych+liczbaBlednych);
        if(tpr.isNaN()) return 0.0;
        else return tpr;
    }



}

