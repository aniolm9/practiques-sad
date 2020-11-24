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


#include "note.h"
#include "ui_note.h"
#include <QMessageBox>

/* Class constructor. */
Note::Note(QWidget *parent, int id, QString name, QString data): QMainWindow(parent), ui(new Ui::Note) {
    ui->setupUi(this);
    this->id = id;
    this->ui->lineEdit->setText(name);
    this->ui->textEdit->setText(data);
    this->saved = true;
    this->ui->saveNote->setDisabled(this->saved);
    this->ui->lineEdit->setFocus();
}

/* Class destructor. */
Note::~Note() {
    delete ui;
}


/* Override closeEvent to check if the note is saved
 * and also notify the dashboard that it can get the
 * focus back.
 */
void Note::closeEvent(QCloseEvent *event) {
    if (!this->saved) {
        QMessageBox msgBox;
        msgBox.setWindowTitle(tr("Note"));
        msgBox.setText(tr("The document has been modified."));
        msgBox.setInformativeText(tr("Do you want to save your changes?"));
        msgBox.setStandardButtons(QMessageBox::Save | QMessageBox::Discard | QMessageBox::Cancel);
        msgBox.setDefaultButton(QMessageBox::Save);
        int ret = msgBox.exec();
        switch (ret) {
          case QMessageBox::Save:
              // Save was clicked
              on_saveNote_clicked();
              break;
          case QMessageBox::Discard:
              // Don't Save was clicked
              break;
          default:
              // Cancel, cross, etc.
              event->ignore();
              return;
        }
    }
    emit giveFocus();
    event->accept();
}


/* Slots */
/* Closes the note. */
void Note::on_closeNote_clicked() {
    this->close();
}

/* Enable save button if the text is modified. */
void Note::on_textEdit_textChanged() {
    this->saved = false;
    this->ui->saveNote->setDisabled(this->saved);
}

/* Saves the note to the database.
 * If the note was new, it gets updated with the assigned ID.
 */
void Note::on_saveNote_clicked() {
    // Send signal to dashboard.
    QString name = this->ui->lineEdit->text();
    QString data = this->ui->textEdit->toPlainText();
    int result = emit save(this->id, name, data);
    if (result != constants::SAVE_ERROR) {
        this->id = result;
        this->saved = true;
        this->ui->saveNote->setDisabled(this->saved);
    } else {
        QMessageBox msgBox;
        msgBox.setWindowTitle(tr("Fatal error"));
        msgBox.setText(tr("The database is corrupted."));
        msgBox.setStandardButtons(QMessageBox::Close);
        msgBox.setDefaultButton(QMessageBox::Close);
        msgBox.exec();
        QCoreApplication::quit();
    }
}

/* Like on_textEdit_textChanged() but for the note name */
void Note::on_lineEdit_textEdited(const QString &arg1) {
    this->saved = false;
    this->ui->saveNote->setDisabled(this->saved);
    Q_UNUSED(arg1); // STFU
}
