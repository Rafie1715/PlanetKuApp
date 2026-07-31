# 🌍 PlanetKu

**PlanetKu** adalah aplikasi Android manajemen sampah pintar (*smart waste management*) yang dilengkapi dengan fitur klasifikasi AI dan kalkulator emisi karbon. Dikembangkan sebagai proyek *capstone* Bangkit Academy, PlanetKu bertujuan untuk mendorong keberlanjutan lingkungan dengan membuat pemilahan sampah, estimasi harga, dan pelacakan jejak karbon menjadi lebih mudah dan interaktif bagi masyarakat.

## ✨ Fitur Utama

*   ♻️ **Klasifikasi Sampah AI**: Gunakan kamera perangkat Anda untuk mengklasifikasikan berbagai jenis sampah secara instan menggunakan model *Machine Learning* TensorFlow Lite (`model_harga_sampah.tflite`).
*   💰 **Prediksi Harga (Price Prediction)**: Dapatkan estimasi harga untuk material sampah yang dapat didaur ulang sebelum disetorkan.
*   ☁️ **Kalkulator Emisi Karbon**: Hitung dan pantau emisi karbon harian Anda untuk membantu membangun kebiasaan yang lebih ramah lingkungan.
*   🗺️ **Peta Lokasi Sampah (Maps)**: Temukan bank sampah dan lokasi pembuangan terdekat di sekitar Anda menggunakan integrasi peta.
*   📰 **Artikel Edukasi**: Baca artikel dan berita pilihan mengenai pelestarian lingkungan dan keberlanjutan.
*   🔐 **Autentikasi Pengguna**: Fungsionalitas masuk dan daftar (*Login/Register*) yang aman dan mudah.
*   👤 **Manajemen Profil**: Kelola informasi pribadi dan pantau progres dampak lingkungan Anda.

## 🛠️ Teknologi yang Digunakan

*   **Bahasa Pemrograman**: [Kotlin](https://kotlinlang.org/)
*   **Arsitektur**: MVVM (Model-View-ViewModel)
*   **Machine Learning**: [TensorFlow Lite](https://www.tensorflow.org/lite) 
*   **Backend & Database**: Firebase (Authentication, Firestore)
*   **Peta/Lokasi**: Google Maps SDK
*   **UI Components**: Material Components for Android

## 🚀 Panduan Instalasi

Ikuti langkah-langkah berikut untuk mengatur dan menjalankan proyek ini di komputer lokal Anda.

### Prasyarat

*   Android Studio (Versi terbaru direkomendasikan)
*   Android SDK
*   Proyek Firebase aktif untuk mendapatkan file konfigurasi `google-services.json`.

### Langkah-langkah

1.  **Clone repositori**
    ```bash
    git clone https://github.com/username-anda/PlanetKuApp.git
    cd PlanetKuApp
    ```

2.  **Tambahkan Konfigurasi Firebase**
    Dapatkan file `google-services.json` dari konsol Firebase Anda dan letakkan di dalam direktori `app/` agar layanan autentikasi dan database berfungsi:
    ```text
    PlanetKuApp/
    └── app/
        └── google-services.json
    ```

3.  **Build Proyek**
    Buka proyek di Android Studio. Tunggu hingga proses sinkronisasi Gradle selesai dan mengunduh semua dependensi yang diperlukan.

4.  **Jalankan Aplikasi**
    Hubungkan emulator Android atau perangkat fisik Anda, lalu klik tombol **Run** (Shift + F10) di Android Studio.

## 📂 Struktur Utama Proyek

*   `ui/classification/`: Berisi implementasi antarmuka kamera dan *helper* klasifikasi sampah berbasis AI.
*   `ui/carbonemission/`: Antarmuka dan logika perhitungan untuk fitur kalkulator jejak karbon.
*   `ui/priceprediction/`: Layar dan fungsionalitas untuk mengestimasi nilai/harga sampah.
*   `ui/maps/`: Menangani integrasi Google Maps untuk menampilkan lokasi pembuangan sampah terdekat.
*   `ui/article/`: Komponen UI untuk memuat dan membaca artikel informatif.
*   `data/`: Struktur *Data Layer* yang memuat koneksi repository dan *seeder* ke Firestore.
*   `assets/`: Menyimpan file statis seperti label klasifikasi (`labels.txt`) dan model ML (`model_harga_sampah.tflite`).
