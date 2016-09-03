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

package de.medusalix.biblios.controls;

import de.medusalix.biblios.utils.BackupUtils;
import javafx.scene.control.ListCell;

public class BackupCell extends ListCell<BackupUtils.Backup>
{
    @Override
    protected void updateItem(BackupUtils.Backup item, boolean empty)
    {
        super.updateItem(item, empty);
        
        if (!empty)
        {
            setText(item.getName());
        }
        
        else
        {
            setText(null);
        }
    }
}
