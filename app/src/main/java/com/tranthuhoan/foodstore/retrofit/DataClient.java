package com.tranthuhoan.foodstore.retrofit;


import com.tranthuhoan.foodstore.model.Food;
import com.tranthuhoan.foodstore.model.Manager;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


//Config Method Send Data To Server
public interface DataClient {
//    //Student
//    @FormUrlEncoded
//    @POST("insert.php")
//    Call<String> InsertStudentData(@Field("StudentName") String StudentName,
//                                   @Field("StudentEmail") String StudentEmail,
//                                   @Field("StudentPassword") String StudentPassword,
//                                   @Field("StudentAvatar") String StudentAvatar);
//
//    @Multipart
//    @POST("uploadImage.php")
//    Call<String> UploadStudentPhoto(@Part MultipartBody.Part photo);
//
//    @FormUrlEncoded
//    @POST("login.php")
//    Call<List<Student>> LoginStudentData(@Field("StudentEmail") String StudentEmail,
//                                         @Field("StudentPassword") String StudentPassword);
//
//    @FormUrlEncoded
//    @POST("update.php")
//    Call<String> UpdateStudentData(@Field("StudentId") String StudentId,
//                                   @Field("StudentNo") String StudentNo,
//                                   @Field("StudentName") String StudentName,
//                                   @Field("StudentDOB") String StudentDOB,
//                                   @Field("StudentClass") String StudentClass,
//                                   @Field("StudentPhone") String StudentPhone,
//                                   @Field("StudentEmail") String StudentEmail,
//                                   @Field("StudentAvatar") String StudentAvatar,
//                                   @Field("StudentGender") String StudentGender,
//                                   @Field("StudentCurrentAvatar") String StudentCurrentAvatar);
//
//    @GET("delete.php")
//    Call<String> DeleteStudentData(@Query("StudentId") String StudentId, @Query("StudentAvatar") String StudentAvatar);
//
//    @FormUrlEncoded
//    @POST("changePassword.php")
//    Call<String> ChangePasswordStudentData(@Field("StudentId") String StudentId,
//                                           @Field("StudentNewPassword") String StudentNewPassword);
//
//    //Report
//    @FormUrlEncoded
//    @POST("report.php")
//    Call<String> ReportStudentData(@Field("StudentId") String StudentId,
//                                   @Field("StudentReport") String StudentReport);

    //Manager
    @FormUrlEncoded
    @POST("manager/login.php")
    Call<List<Manager>> LoginManagerData(@Field("ManagerEmail") String ManagerEmail,
                                         @Field("ManagerPassword") String ManagerPassword);

    @GET("manager/delete.php")
    Call<String> DeleteManagerData(@Query("ManagerId") String ManagerId, @Query("ManagerAvatar") String ManagerAvatar);

    @Multipart
    @POST("manager/uploadImage.php")
    Call<String> UploadManagerPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("manager/update.php")
    Call<String> UpdateManagerData(@Field("ManagerId") String ManagerId,
                                   @Field("ManagerEmail") String ManagerEmail,
                                   @Field("ManagerName") String ManagerName,
                                   @Field("ManagerPhone") String ManagerPhone,
                                   @Field("ManagerAvatar") String ManagerAvatar,
                                   @Field("ManagerCurrentAvatar") String ManagerCurrentAvatar);

    //    @FormUrlEncoded
//    @POST("admin/forgotPassword.php")
//    Call<String> ForgotPasswordManagerData(@Field("ManagerId") String ManagerId,
//                                         @Field("ManagerEmail") String ManagerEmail,
//                                         @Field("ManagerNewPassword") String ManagerNewPassword);
//
//    @GET("admin/delete.php")
//    Call<String> DeleteManagerData(@Query("ManagerId") String ManagerId, @Query("ManagerAvatar") String ManagerAvatar);
//
//    @FormUrlEncoded
//    @POST("admin/changePassword.php")
//    Call<String> ChangePasswordManagerData(@Field("ManagerId") String ManagerId,
//                                         @Field("ManagerNewPassword") String ManagerNewPassword);
//
//    //Manager Manager
//    //Add Student
//    @FormUrlEncoded
//    @POST("admin/addStudent.php")
//    Call<String> ManagerAddStudentData(@Field("StudentNo") String StudentNo,
//                                     @Field("StudentName") String StudentName,
//                                     @Field("StudentDOB") String StudentDOB,
//                                     @Field("StudentClass") String StudentClass,
//                                     @Field("StudentGender") String StudentGender,
//                                     @Field("StudentPhone") String StudentPhone,
//                                     @Field("StudentEmail") String StudentEmail,
//                                     @Field("StudentPassword") String StudentPassword,
//                                     @Field("StudentActive") String StudentActive,
//                                     @Field("StudentAvatar") String StudentAvatar);
//
    //View All
    @POST("manager/viewAllFood.php")
    Call<List<Food>> ManagerViewAllFoodData();
//
//    //Update
//    @FormUrlEncoded
//    @POST("admin/updateStudent.php")
//    Call<String> ManagerUpdateStudentData(@Field("StudentId") String StudentId,
//                                        @Field("StudentNo") String StudentNo,
//                                        @Field("StudentName") String StudentName,
//                                        @Field("StudentDOB") String StudentDOB,
//                                        @Field("StudentClass") String StudentClass,
//                                        @Field("StudentPhone") String StudentPhone,
//                                        @Field("StudentEmail") String StudentEmail,
//                                        @Field("StudentAvatar") String StudentAvatar,
//                                        @Field("StudentGender") String StudentGender,
//                                        @Field("StudentPassword") String StudentPassword,
//                                        @Field("StudentActive") String StudentActive,
//                                        @Field("StudentCurrentAvatar") String StudentCurrentAvatar);
//
//    //Send Notice
//    @FormUrlEncoded
//    @POST("admin/sendNotice.php")
//    Call<String> ManagerNoticeToStudentData(@Field("StudentNotice") String StudentNotice);
//
//    //Reply
//    @FormUrlEncoded
//    @POST("admin/replyStudent.php")
//    Call<String> ManagerReplyStudentData(@Field("StudentId") String StudentId,
//                                       @Field("StudentReply") String StudentReply);
}
