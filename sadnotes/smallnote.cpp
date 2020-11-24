/*
 * Copyright (c) 2020, Aniol Marti & Hugo Martínez
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You should have received a copy of the License. If not,
 * see <https://opensource.org/licenses/BSD-3-Clause>.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include "smallnote.h"
#include <QTextBlock>

/* Class constructor. */
SmallNote::SmallNote(int id, QString name, QString data): QObject() {
    this->id = id;
    this->name = name;
    this->data = data;
    /* QTextEdit field to be displayed in dashboard */
    this->view = new QTextEdit();
    this->view->setObjectName("view");
    this->view->append("<html><b>" + name + "</b></html>");
    this->view->append(data);
    QObject::connect(this->view, &QTextEdit::textChanged, this, &SmallNote::on_view_textChanged);
}

/* Class destructor. */
SmallNote::~SmallNote() {
    delete this->view;
}

/* Getters and setters. */
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


/* Slots. */
/* If the text of a note is updated, we save it to the class properties. */
void SmallNote::on_view_textChanged() {
    QStringList lines = this->view->toPlainText().split('\n', Qt::SkipEmptyParts);
    if (!lines.empty()) {
        this->name = lines.at(0);
        lines.removeAt(0);
        this->data = lines.join('\n');
    } else {
        this->name = "";
        this->data = "";
    }
    /* There is no clean way to ensure that the first line
     * is bold and the others aren't. This solution iterates
     * over all lines and sets the first one to bold. To do that,
     * we need another loop that iterates the line.
     */
    int numBlocks = this->view->document()->blockCount();
    this->view->blockSignals(true);
    for (int i = 0; i < numBlocks; i++) {
        QTextCursor cursor(this->view->document()->findBlockByNumber(i));
        for (QTextBlock::iterator it = cursor.block().begin(); !(it.atEnd()); ++it) {
            QTextCharFormat charFormat = it.fragment().charFormat();
            QFont font = charFormat.font();
            font.setBold(!i);
            charFormat.setFont(font);

            QTextCursor tempCursor = cursor;
            tempCursor.setPosition(it.fragment().position());
            tempCursor.setPosition(it.fragment().position() + it.fragment().length(), QTextCursor::KeepAnchor);
            tempCursor.setCharFormat(charFormat);
        }
    }
    this->view->blockSignals(false);
    this->updated = true;
}
