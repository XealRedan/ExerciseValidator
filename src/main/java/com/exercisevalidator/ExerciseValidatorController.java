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
import com.exercisevalidator.model.FileMetaList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Controller for exercise validator
 */
@Controller
public class ExerciseValidatorController {

    private static final String OUTPUT_FILEPATH = "D:/tmp/";

    private FileMetaList files = new FileMetaList();

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
    @RequestMapping(value="/upload/", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public @ResponseBody
    FileMetaList postUpload(
            MultipartHttpServletRequest request,
            HttpServletResponse response) {
        Iterator<String> itr = request.getFileNames();

        while (itr.hasNext()) {
            MultipartFile mpf = request.getFile(itr.next());

            final FileMeta fileMeta = new FileMeta();
            fileMeta.setName(mpf.getOriginalFilename());
            fileMeta.setSize(mpf.getSize() / 1024 + " kB");
            fileMeta.setType(mpf.getContentType());
            fileMeta.setUrl(request.getContextPath() + "/files/" + mpf.getOriginalFilename());
            fileMeta.setDeleteUrl(request.getContextPath() + "/delete/" + mpf.getOriginalFilename());

            try {
                fileMeta.setBytes(mpf.getBytes());

                FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(OUTPUT_FILEPATH + mpf.getOriginalFilename()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            files.getFiles().add(fileMeta);
        }

        return files;
    }

    @RequestMapping(value="/upload/", method = RequestMethod.GET)
    public @ResponseBody
    FileMetaList getUpload(HttpServletRequest request, HttpServletResponse response) {
        return files;
    }

    @RequestMapping(value="/delete/{file}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteFile(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("file") String file) {
        // Delete the file
        // TODO

        // Inform that the file has been deleted
        String answer = "{\"files\": [\n";

        answer += "\t{\n";
        answer += "\t\t\"" + file + "\": true\n";
        answer += "\t}\n";
        answer += "]}";

        return answer;
    }
}
