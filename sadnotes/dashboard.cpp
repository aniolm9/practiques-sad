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
    qDebug() << this->lastFocused;
    /* If the last focus before clicking to remove was a note, we remove it */
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

/* Opens a note in a new window. */
void Dashboard::on_open_clicked() {
    /* If the last focus before clicking to open was a note, we open it */
    if (dynamic_cast<QTextEdit*>(this->lastFocused)) {
        QMutableVectorIterator<SmallNote*> it(this->sns);
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

/* Every time that a focusChange signal is emited, we capture it
 * and store the old value. It is needed to know which note has to be
 * opened or removed when pressin the button.
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
