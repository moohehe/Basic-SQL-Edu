package com.basicsqledu.www.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CertifyInterceptor extends HandlerInterceptorAdapter{

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {
      // TODO Auto-generated method stub
      int k = 0; 
      Cookie [] cok = request.getCookies();
      
      int [] cook = new int[20];
      for(Cookie c : cok){
         if(c.getValue().equals("pass")){
            int temp = Integer.valueOf(c.getName().replace("completeStage",""));
            cook[temp-1] = 1; 
         }
      }
      for(int i : cook){
         if(i == 1){
            k++;
         }
      }
      System.out.println("k="+k);
      if(k >= 19){
         //인증서 가자 gg
         System.out.println("20 stage 전부 클리어함");
         return super.preHandle(request, response, handler);         // == return true;   (갈 길 가라)

      }else{
         response.sendRedirect("/www");
         return false;
      }
   }

}