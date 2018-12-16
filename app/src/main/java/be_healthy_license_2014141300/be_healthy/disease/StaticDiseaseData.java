package be_healthy_license_2014141300.be_healthy.disease;

import com.be_healthy_license_2014141300.be_healthy.disease.Disease;

import java.util.ArrayList;

public class StaticDiseaseData {

    public static ArrayList<Disease> diseases= new ArrayList<>();

    public Disease getDisease(String name){
            for(Disease disease:diseases){
                if (disease.getName().equals(name)){
                    return disease;
                }
            }
            return null;
        }

        public void fill(ArrayList<Disease> data){
            diseases=data;
        }
}
