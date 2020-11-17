#include "note.h"
#include "ui_note.h"
#include <QMessageBox>

Note::Note(QWidget *parent, int id): QMainWindow(parent), ui(new Ui::Note)
{
    ui->setupUi(this);
    this->id = id;
    this->ui->saveNote->setDisabled(this->saved);
}

Note::~Note()
{
    delete ui;
}

/* Actions and slots */
void Note::on_closeNote_clicked()
{
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

void Note::on_textEdit_textChanged()
{
    this->saved = false;
    this->ui->saveNote->setDisabled(this->saved);
}

void Note::on_saveNote_clicked()
{
    // Send signal to dashboard.
    QString name = this->ui->lineEdit->text();
    QString data = this->ui->textEdit->toPlainText();
    this->saved = emit save(this->id, name, data);
    this->ui->saveNote->setDisabled(this->saved);
}


void Note::on_lineEdit_textEdited(const QString &arg1)
{
    this->saved = false;
    this->ui->saveNote->setDisabled(this->saved);
    Q_UNUSED(arg1); // STFU
}
