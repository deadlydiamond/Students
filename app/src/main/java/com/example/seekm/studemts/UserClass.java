package com.example.seekm.studemts;

public class UserClass {

    public String First_Name,Last_Name,Email_Address,SignUp_Password,Date_Of_Birth,User_Gender,Profile_Image;
    public String Education_Board,Class_Grade,School_Private,Field_Of_Study,Latest_qualification;
    public String User_Longitude,User_Latitude;

    public UserClass(){

    }


    public UserClass(String first_Name,String last_Name ,String email_Address ,String signUp_Password , String date_Of_Birth , String user_Gender,String profile_Image,String education_Board,String class_Grade,String school_Private,String field_Of_Study,String latest_qualification,String user_Longitude, String user_Latitude){

        First_Name = first_Name;
        Last_Name = last_Name ;
        Email_Address =  email_Address;
        SignUp_Password = signUp_Password;
        Date_Of_Birth = date_Of_Birth;
        User_Gender= user_Gender;
        Profile_Image = profile_Image;
        Education_Board = education_Board;
        Class_Grade = class_Grade;
        School_Private = school_Private;
        Field_Of_Study=field_Of_Study;
        Latest_qualification=latest_qualification;
        User_Longitude = user_Longitude;
        User_Latitude = user_Latitude;


    }

}
