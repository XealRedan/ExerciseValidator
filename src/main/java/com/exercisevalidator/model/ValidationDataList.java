package com.exercisevalidator.model;

/*
 * #%L
 * Exercise validator
 * %%
 * Copyright (C) 2015 Alexandre Lombard
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of validation data
 */
public class ValidationDataList {
    private List<ValidationData> validationDataList = new ArrayList<>();

    public List<ValidationData> getValidationDataList() {
        return this.validationDataList;
    }

    public void setValidationDataList(List<ValidationData> validationDataList) {
        this.validationDataList = validationDataList;
    }
}
