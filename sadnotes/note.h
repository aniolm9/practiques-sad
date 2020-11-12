#ifndef NOTE_H
#define NOTE_H

#include <QMainWindow>
#include <string>

QT_BEGIN_NAMESPACE
namespace Ui { class Note; }
QT_END_NAMESPACE

class Note : public QMainWindow
{
    Q_OBJECT

public:
    Note(QWidget *parent = nullptr, std::string notesPath = "");
    ~Note();

private slots:
    void on_closeNote_clicked();

private:
    Ui::Note *ui;
    QString currentNote;
    std::string notesPath;
};
#endif // NOTE_H
