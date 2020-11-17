#include "dialog.h"
#include "ui_dialog.h"

Dialog::Dialog(QWidget *parent, bool error) : QDialog(parent), ui(new Ui::Dialog)
{
    ui->setupUi(this);
    this->error = error;
}

Dialog::~Dialog()
{
    delete ui;
}

void Dialog::setLabel(QString text) {
    ui->label->setText(text);
}

void Dialog::on_pushButton_clicked()
{
    close();
    if (this->error) {
        QCoreApplication::quit();
    }
}
