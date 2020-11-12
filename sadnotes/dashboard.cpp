#include "dashboard.h"
#include "ui_dashboard.h"
#include "note.h"
#include <iostream>

Dashboard::Dashboard(QWidget *parent, std::string notesPath): QMainWindow(parent), ui(new Ui::Dashboard)
{
    ui->setupUi(this);
    this->notesPath = notesPath;
}

Dashboard::~Dashboard()
{
    delete ui;
}

/* Actions and slots */
void Dashboard::on_newNote_clicked()
{
    Note *note = new Note(this);
    note->show();
}
