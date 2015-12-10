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
function qs(key) {
    key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&"); // escape RegEx meta chars
    var match = location.search.match(new RegExp("[?&]"+key+"=([^&]+)(&|$)"));
    return match && decodeURIComponent(match[1].replace(/\+/g, " "));
}

(function () {
    'use strict';

    var validationUrl = '/ExerciseValidator/validate/';

    $( '.btn-exercise-validation').on(
        'click',
        function() {
            // Append a progress bar to the exercise-validation class
            $('<div class="progress-circle-indeterminate"></div>').appendTo('.progress-exercise-validation');

            var exerciseId = qs('id');
            var exerciseValidationUrl =
                exerciseId === null ?
                    validationUrl :
                    validationUrl + ('?id=' + exerciseId);

            // Make the ajax request
            $.ajax({
                url: exerciseValidationUrl,
                method: 'GET',
                dataType: 'json',

                success: function(data) {
                    // Clear the content of the table
                    $('.table-exercise-validation').html('');

                    $('<thead>' +
                        '<tr><th>Input file</th><th>Output file</th><th>Progress</th></tr>' +
                        '</thead>')

                    // Update the content with validation results
                    $.each(data.validationDataList, function (index, element) {
                        // Define the class of the row
                        var successClass;
                        if(element.successRate === 0)
                            successClass = 'danger';
                        else if (element.successRate === 1)
                            successClass = 'success';
                        else
                            successClass = 'warning';

                        var progressCell =
                            '<div class="progress">' +
                                '<input class="progress-circle" data-pages-progress="circle" value="' + Math.floor(100 * element.successRate) + '" type="hidden" data-color="complete">' +
                            '</div>';


                        // Append the row
                        $('<tr class="' + successClass +'">' +
                            '<td>' + element.inputFile + '</td>' +
                            '<td>' + element.outputFile + '</td>' +
                            '<td>' + Math.floor(100 * element.successRate) + '</td>' +
                            '</tr>').appendTo('.table-exercise-validation');
                    });
                },

                error: function() {
                    // Append the row
                    $('<tr class="danger">' +
                        '<td><strong>Unexpected error:</strong> unable to validate exercise</td>' +
                        '</tr>').appendTo('.table-exercise-validation');
                },

                complete: function() {
                    // Clear the progress bar
                    $('.progress-exercise-validation').html('');
                }
            });
        }
    );
}());