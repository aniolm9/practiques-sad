#ifndef SMALLNOTE_H
#define SMALLNOTE_H

#include <QObject>
#include <QString>
#include <QTextEdit>

QT_BEGIN_NAMESPACE
namespace Ui {
    class SmallNote;
}
QT_END_NAMESPACE

class SmallNote: public QObject {
    Q_OBJECT

    public:
        SmallNote(int id, QString name, QString data);
        ~SmallNote();
        int getId();
        bool getStatus();
        QString getName();
        QString getData();
        QTextEdit* getText();
        void setStatus(bool status);

    private:
        int id;
        bool updated = false;
        QString name;
        QString data;
        QTextEdit *view;

    private slots:
        void on_view_textChanged();
};
#endif // SMALLNOTE_H
