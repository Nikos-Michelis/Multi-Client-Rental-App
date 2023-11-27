package com.nick.server.rental_crud_utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public interface IDRecordHandler {
     <T> T findMaxRecordID(List<Map<String, Object>> loadedRentalArrayList,
                           T inputObject, BiConsumer<T, Integer> setter, String getterMethodName);
     default <T> T findMaxRecordIDDefault(List<Map<String, Object>> loadedRentalArrayList,
                                          T inputObject, BiConsumer<T, Integer> setter, String getterMethodName) {
          int maxId = 0;

          if (!loadedRentalArrayList.isEmpty()) {
               for (Map<String, Object> rentalInfo : loadedRentalArrayList) {
                    if (rentalInfo.containsKey(inputObject.getClass().getSimpleName())) {
                         T loadedRentalObject = (T) rentalInfo.get(inputObject.getClass().getSimpleName());
                         try {
                              Method getterMethod = loadedRentalObject.getClass().getMethod(getterMethodName);
                              int currentValue = (int) getterMethod.invoke(loadedRentalObject);
                              maxId = Math.max(maxId, currentValue);
                         } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                              e.printStackTrace();
                         }
                    }
               }
          }
          setter.accept(inputObject, maxId + 1);
          return inputObject;
     }
}
