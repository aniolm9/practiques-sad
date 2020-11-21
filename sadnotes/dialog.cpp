#include "dialog.h"
#include "ui_dialog.h"

/* Class constructor. */
Dialog::Dialog(QWidget *parent, bool error) : QDialog(parent), ui(new Ui::Dialog) {
    ui->setupUi(this);
    this->error = error;
}

/* Class destructor. */
Dialog::~Dialog() {
    delete ui;
}

/* Updates dialog text. */
void Dialog::setLabel(QString text) {
    ui->label->setText(text);
}

/* Closes the dialog. */
void Dialog::on_pushButton_clicked() {
    close();
    if (this->error) {
        QCoreApplication::quit();
    }
}
