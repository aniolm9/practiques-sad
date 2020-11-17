#include "dashboard.h"
#include "ui_dashboard.h"
#include "note.h"
#include <iostream>

Dashboard::Dashboard(QWidget *parent, Database *db): QMainWindow(parent), ui(new Ui::Dashboard)
{
    ui->setupUi(this);
    this->database = db;

}

Dashboard::~Dashboard()
{
    delete ui;
}

/* Actions and slots */
void Dashboard::on_newNote_clicked()
{
    Note *note = new Note(this);
    QObject::connect(note, &Note::save, this, &Dashboard::saveNote);
    note->show();
}

bool Dashboard::saveNote(int id, QString name, QString data) {
    qDebug() << "Save note";
    // New note => Insert.
    if (id == -1) {
        return this->database->insertNote(name, data);
    }
    // Already existing note => Update.
    else {
        return this->database->updateNote(id, name, data);
    }
}
