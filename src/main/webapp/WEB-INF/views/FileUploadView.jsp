<%--
  #%L
  Exercise validator
  %%
  Copyright (C) 2015 Alexandre Lombard
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Exercise Validator</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/webjars/blueimp-gallery/2.16.0/css/blueimp-gallery.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/webjars/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/webjars/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/css/jquery.fileupload.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/css/jquery.fileupload-ui.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/css/style.css">
</head>
<body>
<div class="container">
    <h1>Exercise validator</h1>

    <p class="lead">Web utility to validate student tests</p>

    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload"
          action="<%=request.getContextPath()%>/upload/"
          method="POST" enctype="multipart/form-data" data-ng-app="file-upload"
          data-ng-controller="InternalFileUploadController" data-file-upload="options"
          data-ng-class="{'fileupload-processing': processing() || loadingFiles}">
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button" ng-class="{disabled: disabled}">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>Add files...</span>
                    <input type="file" name="files[]" multiple ng-disabled="disabled">
                </span>
                <button type="button" class="btn btn-primary start" data-ng-click="submit()">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Start upload</span>
                </button>
                <button type="button" class="btn btn-warning cancel" data-ng-click="cancel()">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel upload</span>
                </button>
                <a href="<%=request.getContextPath()%>/validate/" role="button" class="btn btn-success disabled">
                    <i class="glyphicon glyphicon-ok"></i>
                    <span>Validate</span>
                </a>
                <!-- The global file processing state -->
                <span class="fileupload-process"></span>
            </div>
            <!-- The global progress state -->
            <div class="col-lg-5 fade" data-ng-class="{in: active()}">
                <!-- The global progress bar -->
                <div class="progress progress-striped active" data-file-upload-progress="progress()">
                    <div class="progress-bar progress-bar-success" data-ng-style="{width: num + '%'}"></div>
                </div>
                <!-- The extended global progress state -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <!-- The table listing the files available for upload/download -->
        <table class="table table-striped files ng-cloak">
            <tr data-ng-repeat="file in queue" data-ng-class="{'processing': file.$processing()}">
                <td data-ng-switch data-on="!!file.thumbnailUrl">
                    <div class="preview" data-ng-switch-when="true">
                        <a data-ng-href="{{file.url}}" title="{{file.name}}" download="{{file.name}}" data-gallery><img
                                data-ng-src="{{file.thumbnailUrl}}" alt=""></a>
                    </div>
                    <div class="preview" data-ng-switch-default data-file-upload-preview="file"></div>
                </td>
                <td>
                    <p class="name" data-ng-switch data-on="!!file.url">
                        <span data-ng-switch-when="true" data-ng-switch data-on="!!file.thumbnailUrl">
                            <a data-ng-switch-when="true" data-ng-href="{{file.url}}" title="{{file.name}}"
                               download="{{file.name}}" data-gallery>{{file.name}}</a>
                            <a data-ng-switch-default data-ng-href="{{file.url}}" title="{{file.name}}"
                               download="{{file.name}}">{{file.name}}</a>
                        </span>
                        <span data-ng-switch-default>{{file.name}}</span>
                    </p>
                    <strong data-ng-show="file.error" class="error text-danger">{{file.error}}</strong>
                </td>
                <td>
                    <p class="size">{{file.size | formatFileSize}}</p>

                    <div class="progress progress-striped active fade" data-ng-class="{pending: 'in'}[file.$state()]"
                         data-file-upload-progress="file.$progress()">
                        <div class="progress-bar progress-bar-success" data-ng-style="{width: num + '%'}"></div>
                    </div>
                </td>
                <td>
                    <button type="button" class="btn btn-primary start" data-ng-click="file.$submit()"
                            data-ng-hide="!file.$submit || options.autoUpload"
                            data-ng-disabled="file.$state() == 'pending' || file.$state() == 'rejected'">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span>Start</span>
                    </button>
                    <button type="button" class="btn btn-warning cancel" data-ng-click="file.$cancel()"
                            data-ng-hide="!file.$cancel">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>Cancel</span>
                    </button>
                    <button data-ng-controller="FileDestroyController" type="button" class="btn btn-danger destroy"
                            data-ng-click="file.$destroy()" data-ng-hide="!file.$destroy">
                        <i class="glyphicon glyphicon-trash"></i>
                        <span>Delete</span>
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>

<script src="<%=request.getContextPath()%>/webjars/jquery/2.1.4/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/webjars/angularjs/1.4.8/angular.min.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-ui/1.11.4/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/webjars/blueimp-load-image/1.14.0/js/load-image.all.min.js"></script>
<script src="<%=request.getContextPath()%>/webjars/blueimp-canvas-to-blob/2.1.1/js/canvas-to-blob.min.js"></script>
<script src="<%=request.getContextPath()%>/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/webjars/blueimp-gallery/2.16.0/js/blueimp-gallery.min.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.iframe-transport.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload-process.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload-image.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload-audio.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload-video.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload-validate.js"></script>
<script src="<%=request.getContextPath()%>/webjars/jquery-file-upload/9.10.1/js/jquery.fileupload-angular.js"></script>
<script src="<%=request.getContextPath()%>/js/FileUpload.js"></script>

</body>
</html>
