#include "smallnote.h"
#include <QDebug>

SmallNote::SmallNote(int id, QString name, QString data): QObject() {
    this->id = id;
    this->name = name;
    this->data = data;
    this->view = new QTextEdit();
    this->view->setObjectName("view");
    this->view->append("<html><b>" + name + "</b></html>");
    this->view->append(data);
    QObject::connect(this->view, &QTextEdit::textChanged, this, &SmallNote::on_view_textChanged);
}

SmallNote::~SmallNote() {
    delete this->view;
}

int SmallNote::getId() {
    return this->id;
}

bool SmallNote::getStatus() {
    return this->updated;
}

QString SmallNote::getName() {
    return this->name;
}
QString SmallNote::getData() {
    return this->data;
}

QTextEdit* SmallNote::getText() {
    return this->view;
}

void SmallNote::setStatus(bool status) {
    this->updated = status;
}

void SmallNote::on_view_textChanged() {
    QStringList lines = this->view->toPlainText().split('\n', QString::SkipEmptyParts);
    this->name = lines.at(0);
    lines.removeAt(0);
    this->data = lines.join('\n');
    this->updated = true;
}
