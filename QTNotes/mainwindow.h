#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QFile>
#include <QFileDialog>
#include <QTextStream>
#include <QMessageBox>
#include <QtPrintSupport/QPrinter>
#include <QtPrintSupport/QPrintDialog>

#include <QtSql>
#include <QDebug>
#include <QFileInfo>


QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public: //funcions per obrir i tancar la connexió al servidor
    QSqlDatabase mydb;
    void tancarConnexio(){
        mydb.close();
        mydb.removeDatabase(QSqlDatabase::defaultConnection);
    }
    bool obrirConnexio(){
        mydb = QSqlDatabase::addDatabase("QSQLITE");
        mydb.setDatabaseName("C:/Users/User/test.db");

        if(!mydb.open()){
            qDebug() << ("Error obrint la base de dades");
            return false;
        }else{
            qDebug() << ("Connectat :) ");
            return true;
        }
    }

public:
    MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_actionNew_triggered();

    void on_actionOpen_triggered();

    void on_actionSave_as_triggered();

    void on_actionExit_triggered();

    void on_actionCopy_triggered();

    void on_actionPaste_triggered();

    void on_actionUndo_triggered();

    void on_actionRedo_triggered();

    void on_actionDelete_triggered();


    void on_pushButton_clicked();

private:
    Ui::MainWindow *ui;


};

#endif // MAINWINDOW_H
