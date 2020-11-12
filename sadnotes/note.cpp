#include "note.h"
#include "ui_note.h"

Note::Note(QWidget *parent, std::string notesPath): QMainWindow(parent), ui(new Ui::Note)
{
    this->notesPath = notesPath;
    ui->setupUi(this);
    currentNote.clear();
}

Note::~Note()
{
    delete ui;
}

/* Actions and slots */
void Note::on_closeNote_clicked()
{
    this->close();
    delete this;
}
