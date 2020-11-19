#include "dashboard.h"
#include "ui_dashboard.h"
#include "note.h"
#include <iostream>

Dashboard::Dashboard(QWidget *parent, Database *db): QMainWindow(parent), ui(new Ui::Dashboard) {
    ui->setupUi(this);
    this->database = db;
    this->updateView();
}

Dashboard::~Dashboard() {
    delete ui;
}

/* Opens a window to create a new note. */
void Dashboard::on_newNote_clicked() {
    Note *note = new Note(this);
    QObject::connect(note, &Note::save, this, &Dashboard::saveNote);
    note->show();
}

/* Slot that gets triggered when a notes needs to be saved. */
int Dashboard::saveNote(int id, QString name, QString data) {
    qDebug() << "Save note";
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

/* Update notes list. */
void Dashboard::updateView() {
    qDebug() << "Updating view";
    QSqlQuery query;
    if (query.exec("SELECT * FROM notes")) {
        /* Clean current layout */
        QLayoutItem *child;
        while ((child = this->ui->scrollAreaWidgetContents->layout()->takeAt(0)) != 0) {
            delete child->widget();
            delete child;
        }
        while(query.next()) {
            QTextBrowser *text = new QTextBrowser();
            text->append(query.value(2).toString());
            this->ui->scrollAreaWidgetContents->layout()->addWidget(text);
        }
    }
}
