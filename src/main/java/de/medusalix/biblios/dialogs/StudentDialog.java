/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios.dialogs;

import de.medusalix.biblios.controls.RestrictedTextField;
import de.medusalix.biblios.Consts;
import de.medusalix.biblios.database.objects.Student;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentDialog extends FormDialog<Student>
{
    private Student student;

    private TextField nameField = (TextField)lookupNode(Consts.FxIds.NAME_FIELD);
    private RestrictedTextField gradeField = (RestrictedTextField)lookupNode(Consts.FxIds.GRADE_FIELD);
    
    private Matcher gradeMatcher = Pattern.compile("([5-9]|1[0-2])[a-z]").matcher("");
    
    private StudentDialog(Image image, String text)
    {
        super(Consts.Paths.STUDENT_DIALOG, image, text);
    
        gradeField.setInputRestriction(grade -> grade.length() <= 3);
        gradeField.setSubmitRestriction(grade -> gradeMatcher.reset(grade).matches());
        
        addTextFields(nameField, gradeField);
    }
    
    public StudentDialog()
    {
        this(Consts.Images.ADD_DIALOG_HEADER, Consts.Dialogs.ADD_STUDENT_TEXT);
    }
    
    public StudentDialog(Student student)
    {
        this(Consts.Images.CHANGE_DIALOG_HEADER, Consts.Dialogs.CHANGE_STUDENT_TEXT);

        this.student = student;

        nameField.setText(student.getName());
        gradeField.setText(student.getGrade());
    }
    
    @Override
    protected boolean isFilled()
    {
        return !nameField.getText().trim().isEmpty() && gradeField.isSubmitRestrictionMet();
    }
    
    @Override
    protected Student getFormResult()
    {
        if (student == null)
        {
            student = new Student();
        }

        student.setName(nameField.getText());
        student.setGrade(gradeField.getText());

        return student;
    }
}
