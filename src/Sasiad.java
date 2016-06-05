/**
 * Created by Mateusz on 2016-05-15.
 */
public class Sasiad {
    double wynikKlasyfikacji;
    int klasaDecyzyjna;
    double odleglosc;

    public Sasiad(double wynikKlasyfikacji,int klasaDecyzyjna){
        this.wynikKlasyfikacji=wynikKlasyfikacji;
        this.klasaDecyzyjna=klasaDecyzyjna;
        this.odleglosc=obliczOdleglosc();
    }

    double obliczOdleglosc(){

        return Math.abs(klasaDecyzyjna-wynikKlasyfikacji);
    }
}
