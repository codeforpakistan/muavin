package com.muaavin.webservices.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class MyJaxbBean
{
  public double user_id;
  
  public MyJaxbBean() {}
  
  public MyJaxbBean(double user_id)
  {
    this.user_id = user_id;
  }
  
  @GET
  @Produces({"application/json"})
  public MyJaxbBean getMyBean() { return new MyJaxbBean(32.0D); }
}
