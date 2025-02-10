## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Sistem Informasi Presensi Siswa (SIPRESIS)

## Prasyarat
- Java 8 atau lebih tinggi
- XAMPP dengan MySQL
- MySQL Connector/J (JDBC Driver)

## Konfigurasi Database
1. Jalankan XAMPP
2. Buka phpMyAdmin
3. Buat database baru bernama `sipresis`
4. Impor `database_setup.sql`

## Dependensi
- JavaFX
- MySQL Connector/J

### Instalasi MySQL Connector/J
1. Download dari [MySQL Website](https://dev.mysql.com/downloads/connector/j/)
2. Tambahkan JAR ke classpath proyek

## Konfigurasi Koneksi Database
Periksa dan sesuaikan koneksi di `DatabaseConnection.java`:
- URL: `jdbc:mysql://localhost:3306/sipresis`
- Username: `root`
- Password: `(kosong)`

## Menjalankan Aplikasi
Pastikan semua dependensi terpasang dan database sudah dikonfigurasi.
