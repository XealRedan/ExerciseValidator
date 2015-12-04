package com.exercisevalidator;

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


import com.exercisevalidator.model.FileMeta;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Controller for exercise validator
 */
@Controller
public class ExerciseValidatorController {

    private static final String OUTPUT_FILEPATH = "D:/tmp/";

    /**
     * Called on index page, returns the file upload page
     * @return the file upload page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showExerciseValidator() {
        return "FileUploadView";
    }

    /**
     * Called when files are uploaded, write them to disk and call the validation page
     * @param request the request
     * @param response the response
     * @return TBD
     */
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public @ResponseBody
    LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        final LinkedList<FileMeta> files = new LinkedList<>();

        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = null;

        while(itr.hasNext()){
            mpf = request.getFile(itr.next());

            final FileMeta fileMeta = new FileMeta();
            fileMeta.setFileName(mpf.getOriginalFilename());
            fileMeta.setFileSize(mpf.getSize()/1024+" kB");
            fileMeta.setFileType(mpf.getContentType());

            try {
                fileMeta.setBytes(mpf.getBytes());

                FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(OUTPUT_FILEPATH + mpf.getOriginalFilename()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            files.add(fileMeta);
        }

        return files;
    }
}
