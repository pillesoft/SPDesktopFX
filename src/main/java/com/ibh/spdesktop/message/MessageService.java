/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.message;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author ihorvath
 */
public final class MessageService {

  private MessageService() { }
  
  
  private static Map<Type, Consumer<Object>> messages = new HashMap<>();

  public static void register(Type type, Consumer<Object> callback) {
    messages.put(type, callback);
  }
//  public static void register(Callable<?> callable) {
//    messages.put(callable.getClass(), callable);
//  }
//  public static void UnRegister(IMessage mess, Method func) {
//    
//  }
  public static void send(Type type, Object arg) {
    Consumer<Object> func = messages.get(type);
    func.accept(arg);
  }
}
