#ifndef DIALOG_H
#define DIALOG_H

#include <QDialog>
#include <QCoreApplication>

QT_BEGIN_NAMESPACE
namespace Ui { class Dialog; }
QT_END_NAMESPACE

class Dialog : public QDialog
{
    Q_OBJECT

public:
    Dialog(QWidget *parent = 0, bool error=false);
    ~Dialog();
    void setLabel(QString text);

private slots:
    void on_pushButton_clicked();

private:
    Ui::Dialog *ui;
    bool error;
};

#endif // DIALOG_H
