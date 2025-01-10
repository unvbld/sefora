# sefora Web App

This application is designed for both store employees and buyers. It manages product data, sales transactions, and shopping carts. The store employees can add, update, and delete product data, as well as manage transactions, while buyers can register, browse products, add items to their shopping cart, and proceed with payment.

## Description

Aplikasi ini digunakan oleh pegawai toko dan pembeli. Data yang diolah adalah data barang-barang yang akan dijual dan data transaksi penjualan.

### Features
- **Pegawai Toko**:
    - Menambahkan, mengubah (edit), dan menghapus data barang yang tersimpan di gudang.
    - Menambahkan dan mengubah (edit) data transaksi penjualan barang.
  
- **Pembeli**:
    - Terdaftar sebelum melakukan pembelian.
    - Sebelum melakukan pembayaran, pembeli dapat menambahkan barang pada keranjang belanja.
    - Pada saat checkout, pegawai toko dapat melihat daftar barang pada keranjang belanja dan memproses pembayaran.

- **Fitur lainnya**:
    - **Jumlah kategori barang** minimal 5 dan harus beragam.
    - **Jenis pembayaran** minimal 3 (misal cash, debit atau saldo).

## Tech Stack

This application is developed with the following technologies:
- **Java 23**
- **Spring Boot** (for backend framework)
- **MySQL** (for database)
- **Thymeleaf** (for frontend templating)
- **Spring Security** (for authentication and authorization)
- **JPA/Hibernate** (for database interaction)
- **Tailwind CSS** (for frontend styling)

## Installation

To run this application locally, follow these steps:

1. **Clone the repository**:

   ```bash
   git clone https://github.com/unvbld/sefora.git
   cd sefora
