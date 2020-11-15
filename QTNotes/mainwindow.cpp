#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QMainWindow>

#include <QSqlDatabase>
#include <QSqlDriver>
#include <QSqlError>
#include <QSqlQuery>
#include <QVariant>
#include <QInputDialog>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    if(!this->obrirConnexio()){
        ui->label->setText("Error obrint la base de dades");

    }else{
        ui->label->setText("Connectat :) ");
    }
}

MainWindow::~MainWindow()
{
    delete ui;
}


void MainWindow::on_actionNew_triggered()
{
   // currentFile.clear();
    ui->textEdit->setText(QString());
}

void MainWindow::on_actionOpen_triggered() // Ara demana el nom de la nota a obrir, es pot adaptar pel dashboard en forma de botó individual per cada nota
{
    QString text="";
    QString nom = QInputDialog::getText(0, "Input dialog","Nom de l'arxiu:", QLineEdit::Normal,"");


    int i=0;
    if(!this->obrirConnexio()){
        qDebug()<<"No s'ha pogut obrir base de dades";
        return;
    }
    this->obrirConnexio();
    QSqlQuery query;
    query.prepare("select * from notes where id='"+nom+"'");
    if(query.exec()){
        int comptador=0;
        while(query.next()){
            comptador++;
        }
        if(comptador>=1){
            query.exec("select note from notes where id='"+nom+"'");
            text += query.value(i).toString(); // encara no funciona
            this->tancarConnexio();
            ui->lineEdit->setText(nom);
            ui->textEdit->setText(text);

        }
        if(comptador<1){
            QMessageBox::warning(this,"Warning","No s'ha pogut obrir la nota");
        }
    }


}


//void MainWindow::on_actionOpen_triggered()
//{
//    QString fileName = QFileDialog::getSaveFileName(this,"open the file");
//    QString nom = QInputDialog::getText(0, "Input dialog",
//                                             "Date of Birth:", QLineEdit::Normal,
//                                             "");
//    QFile file(fileName);
//    currentFile=fileName;
//    if(!file.open(QFile::ReadOnly | QFile::Text)){
//        QMessageBox::warning(this,"Warning","Cannot open file: " + file.errorString());
//        return;
//    }
//    setWindowTitle(fileName);
//    QTextStream in(&file);
//    QString text = in.readAll();
//    ui->textEdit->setText(text);
//    file.close();

//}

void MainWindow::on_actionSave_as_triggered() //Ara guarda la info a la taula "notes" a la base de dades "test.db"
{
    QString name = ui->lineEdit->text();
    QString text = ui->textEdit->toPlainText();

    if(!this->obrirConnexio()){
        qDebug()<<"No s'ha pogut obrir base de dades";
        return;
    }
    this->obrirConnexio();
    QSqlQuery query;
    query.prepare("select * from notes where id='"+name+"'");

    if(query.exec()){
        int comptador=0;
        while(query.next()){
            comptador++;
        }
        if(comptador>=1){
            ui->label->setText("Nota ja existeix");
        }
        if(comptador<1){
            QMessageBox::warning(this,"","S'ha desat la nota");
            ui->label->setText("Nota guardada");
            query.exec("insert into notes values('"+name+"','"+text+"')");
            this->tancarConnexio();
        }
    }
}


//void MainWindow::on_actionSave_as_triggered()
//{
//    QString fileName = QFileDialog::getSaveFileName(this,"save as");
//    QFile file(fileName);
//    if(!file.open(QFile::WriteOnly | QFile::Text)){
//        QMessageBox::warning(this,"Warning","Cannot save file: " + file.errorString());
//        return;
//    }
//      currentFile=fileName;
//      setWindowTitle(fileName);
//      QTextStream out(&file);
//      QString text = ui->textEdit->toPlainText();
//      out << text;
//      file.close();

//}

void MainWindow::on_actionExit_triggered()
{
    QApplication::quit();
}

void MainWindow::on_actionCopy_triggered()
{
    ui->textEdit->copy();
}

void MainWindow::on_actionPaste_triggered()
{
    ui->textEdit->paste();
}

void MainWindow::on_actionUndo_triggered()
{
    ui->textEdit->undo();
}

void MainWindow::on_actionRedo_triggered()
{
    ui->textEdit->redo();
}

void MainWindow::on_actionDelete_triggered()
{
    QString name = ui->lineEdit->text();

    if(!this->obrirConnexio()){
        qDebug()<<"No s'ha pogut obrir base de dades";
        return;
    }
    this->obrirConnexio();
    QSqlQuery query;
    query.prepare("delete from notes where id='"+name+"'");

    if(query.exec()){
        QMessageBox::warning(this,"","S'ha esborrat la nota");
        this->tancarConnexio();
        ui->lineEdit->setText("");
        ui->textEdit->setText("");
    }
    else{
        QMessageBox::warning(this,"error:",query.lastError().text());
    }
}
