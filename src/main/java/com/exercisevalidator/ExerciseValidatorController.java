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
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Controller for exercise validator
 */
@Controller
public class ExerciseValidatorController {

    private static final String UPLOAD_URL = "/upload/";
    private static final String FILES_URL = "/files/";
    private static final String DELETE_URL = "/delete/";

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
        synchronized (this.files) {
            Iterator<String> itr = request.getFileNames();

            while (itr.hasNext()) {
                MultipartFile mpf = request.getFile(itr.next());

                final FileMeta fileMeta = new FileMeta();
                fileMeta.setName(mpf.getOriginalFilename());
                fileMeta.setSize(mpf.getSize() / 1024 + " kB");
                fileMeta.setType(mpf.getContentType());
                fileMeta.setUrl(request.getContextPath() + FILES_URL + mpf.getOriginalFilename());
                fileMeta.setDeleteUrl(request.getContextPath() + DELETE_URL + mpf.getOriginalFilename());

                try {
                    final String path = OUTPUT_FILEPATH + request.getSession().getId() + "/";

                    // Create the directory if it does not exist
                    final File directory = new File(path);
                    if(!directory.exists()) {
                        directory.mkdir();
                    }

                    // Write the file
                    fileMeta.setBytes(mpf.getBytes());

                    FileCopyUtils.copy(mpf.getBytes(),
                            new FileOutputStream(
                                    path + mpf.getOriginalFilename()));
                } catch (IOException e) {
                    fileMeta.setError(e.getMessage());
                }

                this.files.getFiles().add(fileMeta);
            }

            return this.files;
        }
    }

    @RequestMapping(value = FILES_URL + "{file}", method = RequestMethod.GET)
    public @ResponseBody
    FileMeta getFile(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("file") String file) {
        for(FileMeta f : this.files.getFiles()) {
            if(f.getName().equals(file))
                return f;
        }
        return null;
    }

    @RequestMapping(value = FILES_URL, method = RequestMethod.GET)
    public @ResponseBody
    FileMetaList getFiles(HttpServletRequest request, HttpServletResponse response) {
        return this.files;
    }

    @RequestMapping(value = DELETE_URL + "{file:.+}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteFile(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("file") String fileName) {
        final String path = OUTPUT_FILEPATH + request.getSession().getId() + "/";

        boolean success = false;

        // Delete the file
        final File file = new File(path + fileName);
        if(file.exists()) {
            success = file.delete();
        }

        // Remove it from the file meta list (if present)
        for(int idx = 0; idx < this.files.getFiles().size(); idx++) {
            if(this.files.getFiles().get(idx).getName().equals(fileName)) {
                this.files.getFiles().remove(idx);
                break;
            }
        }

        // Inform that the file has been deleted
        String answer = "{\"files\": [\n";

        answer += "\t{\n";
        answer += "\t\t\"" + fileName + "\": " + Boolean.toString(success) + "\n";
        answer += "\t}\n";
        answer += "]}";

        return answer;
    }
}
