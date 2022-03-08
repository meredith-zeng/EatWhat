package com.example.eatwhat.notification;

public class Response {
   private int success;

   public Response(int success) {
      this.success = success;
   }

   public int getSuccess() {
      return success;
   }

   public void setSuccess(int success) {
      this.success = success;
   }

   public Response() {

   }
}
