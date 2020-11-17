#ifndef DASHBOARD_H
#define DASHBOARD_H

#include <QMainWindow>
#include "database.h"
#include "dialog.h"
#include "constants.h"
#include <string>

QT_BEGIN_NAMESPACE
namespace Ui {
    class Dashboard;
}
QT_END_NAMESPACE

class Dashboard : public QMainWindow {
    Q_OBJECT

    public:
        Dashboard(QWidget *parent = nullptr, Database *db = nullptr);
        ~Dashboard();

    private:
        Ui::Dashboard *ui;
        Database *database;

    public slots:
        int saveNote(int id, QString name, QString data);

    private slots:
        void on_newNote_clicked();
};
#endif // DASHBOARD_H
