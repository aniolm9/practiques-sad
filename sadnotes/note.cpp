#include "note.h"
#include "ui_note.h"
#include <QMessageBox>

Note::Note(QWidget *parent, int id): QMainWindow(parent), ui(new Ui::Note) {
    ui->setupUi(this);
    this->id = id;
    this->ui->saveNote->setDisabled(this->saved);
    this->ui->lineEdit->setFocus();
}

Note::~Note() {
    delete ui;
}

/* Closes the note, but checks if there are changes
 * that need to be saved.
 */
void Note::on_closeNote_clicked() {
    // If the current changes are not saved, notify the user.
    if (!this->saved) {
        QMessageBox msgBox;
        msgBox.setText("The document has been modified.");
        msgBox.setInformativeText("Do you want to save your changes?");
        msgBox.setStandardButtons(QMessageBox::Save | QMessageBox::Discard | QMessageBox::Cancel);
        msgBox.setDefaultButton(QMessageBox::Save);
        int ret = msgBox.exec();
        switch (ret) {
          case QMessageBox::Save:
              // Save was clicked
              on_saveNote_clicked();
              this->close();
              break;
          case QMessageBox::Discard:
              // Don't Save was clicked
              this->close();
              break;
          default:
              // Cancel, cross, etc.
              break;
        }
    } else {
        this->close();
    }
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
        msgBox.setText("Fatal error.");
        msgBox.setInformativeText("The database is corrupted.");
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
