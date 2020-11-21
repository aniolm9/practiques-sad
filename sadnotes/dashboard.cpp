#include "dashboard.h"
#include "ui_dashboard.h"
#include <iostream>

Dashboard::Dashboard(QWidget *parent, Database *db): QMainWindow(parent), ui(new Ui::Dashboard) {
    ui->setupUi(this);
    this->database = db;
    this->ui->remove->setDisabled(true);
    this->ui->open->setDisabled(true);
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
    if (query.exec("SELECT * FROM notes ORDER BY id DESC")) {
        /* Clean current layout to avoid memory leaks */
        for (int i = 0; i < this->sns.size(); i++) {
            delete this->sns.at(i);
        }
        this->sns.clear();
        /* Update layout with all the notes in the database */
        while(query.next()) {
            SmallNote *sn = new SmallNote(query.value(0).toInt(), query.value(1).toString(), query.value(2).toString());
            this->sns.append(sn);
            this->ui->scrollAreaWidgetContents->layout()->addWidget(sn->getText());
        }
    }
}

/* Allows saving the notes without having to open them in a new window. */
void Dashboard::on_saveAll_clicked() {
    qDebug() << "Saving";
    for (int i = 0; i < this->sns.size(); i++) {
        if (sns.at(i)->getStatus()) {
            qDebug() << "Saving " << i;
            this->database->updateNote(sns.at(i)->getId(), sns.at(i)->getName(), sns.at(i)->getData());
            /* Here I prefer a direct method call than a signal/slot.
             * It is faster and cleaner.
             */
            sns.at(i)->setStatus(false);
        }
    }
    qDebug() << "Saved";
}

void Dashboard::setLastFocus(QWidget *old, QWidget *now) {
    if (old == nullptr || now == nullptr) return;
    this->lastFocused = old;
    bool isText = !(now->objectName() =="view" || now->objectName() == "remove" || now->objectName() == "open");
    this->ui->remove->setDisabled(isText);
    this->ui->open->setDisabled(isText);
}

void Dashboard::on_remove_clicked() {
    qDebug() << this->lastFocused;
    /* If the last focus before clicking to remove, we remove it */
    if (dynamic_cast<QTextEdit*>(this->lastFocused)) {
        QMutableVectorIterator<SmallNote*> it(this->sns);
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
