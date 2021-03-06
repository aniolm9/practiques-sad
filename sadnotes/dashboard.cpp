/*
 * Copyright (c) 2020, Aniol Marti & Hugo Martínez
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You should have received a copy of the License. If not,
 * see <https://opensource.org/licenses/BSD-3-Clause>.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include "dashboard.h"
#include "ui_dashboard.h"

/* Class constructor. */
Dashboard::Dashboard(QWidget *parent, Database *db): QMainWindow(parent), ui(new Ui::Dashboard) {
    ui->setupUi(this);
    this->database = db;
    this->ui->remove->setDisabled(true);
    this->ui->open->setDisabled(true);
    this->updateView();
}

/* Class destructor. */
Dashboard::~Dashboard() {
    delete ui;
}


/* Slots. */
/* Opens a window to create a new note.
 * Disables the window until the note window is closed.
*/
void Dashboard::on_newNote_clicked() {
    this->createNote();
}

/* Remove the selected note from the database. */
void Dashboard::on_remove_clicked() {
    /* If the last focus before clicking to remove was a note, we remove it */
    if (dynamic_cast<QTextEdit*>(this->lastFocused)) {
        QMutableVectorIterator<SmallNote*> it(this->notes);
        while (it.hasNext()) {
            SmallNote *tmpNote = it.next();
            if (tmpNote->getText() == this->lastFocused) {
                this->database->deleteNote(tmpNote->getId());
                it.remove();
                delete tmpNote;
            }
        }
        this->ui->remove->setDisabled(true);
        this->ui->open->setDisabled(true);
    }
}

/* Allows saving the notes without having to open them in a new window. */
void Dashboard::on_saveAll_clicked() {
    for (auto curNote: this->notes) {
        if (curNote->getStatus()) {
            this->database->updateNote(curNote->getId(), curNote->getName(), curNote->getData());
            /* Here I prefer a direct method call than a signal/slot.
             * It is faster and cleaner.
             */
            curNote->setStatus(false);
        }
    }
}

/* Opens a note in a new window. */
void Dashboard::on_open_clicked() {
    /* If the last focus before clicking to open was a note, we open it */
    if (dynamic_cast<QTextEdit*>(this->lastFocused)) {
        QMutableVectorIterator<SmallNote*> it(this->notes);
        while (it.hasNext()) {
            SmallNote *tmpNote = it.next();
            if (tmpNote->getText() == this->lastFocused) {
                this->createNote(tmpNote->getId(), tmpNote->getName(), tmpNote->getData());
            }
        }
    }
}

/* Slot that gets triggered when a note needs to be saved.
 * If the note is new, inserts it to the database.
 * Otherwise, updated the existing one.
 */
int Dashboard::saveNote(int id, QString name, QString data) {
    int status;
    // New note => Insert.
    if (id == constants::NEW_ID) {
        status = this->database->insertNote(name, data);
    }
    // Already existing note => Update.
    else {
        status = this->database->updateNote(id, name, data);
    }
    updateView();
    return status;
}

/* Every time that a focusChange signal is emited, we capture it
 * and store the old value. It is needed to know which note has to be
 * opened or removed when pressing the button.
 */
void Dashboard::setLastFocus(QWidget *old, QWidget *now) {
    if (old == nullptr || now == nullptr) return;
    this->lastFocused = old;
    bool isText = !(now->objectName() =="view" || now->objectName() == "remove" || now->objectName() == "open");
    this->ui->remove->setDisabled(isText);
    this->ui->open->setDisabled(isText);
}

/* When the scope is returned to the dashboard window enable it again. */
void Dashboard::recoverFocus() {
    this->ui->centralwidget->setDisabled(false);
}


/* Update notes list. */
void Dashboard::updateView() {
    QSqlQuery query;
    if (query.exec("SELECT * FROM notes ORDER BY id DESC")) {
        /* Clean current layout to avoid memory leaks */
        for (auto curNote: this->notes) {
            delete curNote;
        }
        this->notes.clear();
        /* Update layout with all the notes in the database */
        while(query.next()) {
            SmallNote *sn = new SmallNote(query.value(0).toInt(), query.value(1).toString(), query.value(2).toString());
            this->notes.append(sn);
            this->ui->scrollAreaWidgetContents->layout()->addWidget(sn->getText());
        }
    }
}

/* Create a new window with a note.
 * It can open existing notes or create new ones
 */
void Dashboard::createNote(int id, QString name, QString data) {
    Note *note;
    if (id != constants::NEW_ID) {
        note = new Note(this, id, name, data);
    } else {
        note = new Note(this);
    }
    QObject::connect(note, &Note::save, this, &Dashboard::saveNote);
    QObject::connect(note, &Note::giveFocus, this, &Dashboard::recoverFocus);
    this->ui->centralwidget->setDisabled(true);
    note->show();
}
