package com.capstone.planetku.data

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreSeeder {
    fun seedArticles(onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val articles = listOf(
            Article(
                title = "Panduan Mengelola Sampah Rumah Tangga",
                description = "Langkah praktis memilah sampah organik dan anorganik dari dapur Anda.",
                content = "Mengelola sampah rumah tangga adalah langkah awal yang paling krusial dalam menjaga kelestarian bumi. Proses ini dimulai dengan pemilahan sederhana antara sampah organik, seperti sisa makanan dan daun kering, dengan sampah anorganik seperti plastik dan kertas.\n\nSampah organik dapat diolah kembali menjadi kompos yang menyuburkan tanaman, sementara sampah anorganik yang bersih dapat disalurkan ke bank sampah terdekat untuk didaur ulang. Dengan memilah sampah dari sumbernya, kita membantu mengurangi beban Tempat Pembuangan Akhir (TPA) yang kian hari kian menumpuk.\n\nSelain itu, edukasi kepada anggota keluarga lainnya sangat penting agar budaya hidup bersih dan minim sampah dapat menjadi kebiasaan sehari-hari di rumah tangga Anda.",
                url = "https://lingkunganhidup.jakarta.go.id/",
                urlToImage = "https://plus.unsplash.com/premium_photo-1664303498055-662584e03f0b?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-20"
            ),
            Article(
                title = "Bahaya Mikroplastik di Laut Indonesia",
                description = "Limbah plastik berakhir menjadi ancaman bagi ekosistem laut dan kesehatan manusia.",
                content = "Mikroplastik adalah partikel plastik berukuran kurang dari 5mm yang kini banyak ditemukan di perairan Indonesia. Partikel ini berasal dari sampah plastik besar yang terurai di laut akibat paparan sinar matahari dan ombak.\n\nBahaya utama mikroplastik terletak pada kemampuannya menyerap zat kimia beracun. Ketika mikroplastik termakan oleh ikan atau plankton, racun tersebut masuk ke dalam rantai makanan yang akhirnya bisa dikonsumsi oleh manusia. Dampak kesehatan jangka panjang dari akumulasi mikroplastik di tubuh manusia masih terus diteliti oleh para ahli.\n\nOleh karena itu, mengurangi penggunaan plastik sekali pakai bukan hanya tentang menyelamatkan penyu, tetapi juga tentang melindungi kesehatan diri kita dan generasi mendatang.",
                url = "https://www.wwf.id/en/our-work/oceans",
                urlToImage = "https://images.unsplash.com/photo-1618477462146-050d2767eac4?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-18"
            ),
            Article(
                title = "5 Cara Mudah Kurangi Jejak Karbon",
                description = "Pelajari cara sederhana mengurangi kontribusi gas rumah kaca Anda.",
                content = "Jejak karbon adalah jumlah total gas rumah kaca yang dihasilkan oleh aktivitas manusia. Menguranginya tidak selalu membutuhkan perubahan drastis, tetapi konsistensi dalam tindakan kecil.\n\nPertama, gunakan transportasi umum atau bersepeda jika jarak tempuh memungkinkan. Kedua, hematlah energi listrik dengan mematikan lampu and alat elektronik yang tidak digunakan. Ketiga, kurangi konsumsi daging merah karena industri peternakan menyumbang emisi gas metana yang tinggi.\n\nKeempat, beralihlah ke produk lokal untuk mengurangi emisi dari jalur distribusi logistik. Kelima, tanamlah pohon di halaman rumah atau dukung program penghijauan untuk menyerap karbon dioksida di atmosfer.",
                url = "https://www.unep.org/news-and-stories",
                urlToImage = "https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-15"
            ),
            Article(
                title = "Mengenal Konsep Ekonomi Sirkular",
                description = "Memahami sistem produksi yang tidak meninggalkan sampah bagi bumi.",
                content = "Ekonomi sirkular adalah model ekonomi yang berupaya memperpanjang siklus hidup produk. Berbeda dengan model linear 'ambil-buat-buang', ekonomi sirkular mengedepankan prinsip 'reduce, reuse, and recycle'.\n\nDalam sistem ini, limbah dari satu proses produksi dirancang untuk menjadi bahan baku bagi proses lainnya. Contohnya, botol plastik bekas yang diolah menjadi serat kain untuk pakaian. Hal ini meminimalkan pengambilan sumber daya alam baru dan mencegah timbulnya sampah yang tidak perlu.\n\nMendukung ekonomi sirkular berarti kita memilih produk yang tahan lama, mudah diperbaiki, dan dapat didaur ulang sepenuhnya setelah tidak lagi bisa digunakan.",
                url = "https://www.weforum.org/topics/circular-economy",
                urlToImage = "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-12"
            ),
            Article(
                title = "Manfaat Bank Sampah bagi Masyarakat",
                description = "Mengubah sampah menjadi tabungan dan menjaga kebersihan lingkungan.",
                content = "Bank sampah adalah konsep pengumpulan sampah kering dan terpilah yang memiliki nilai ekonomi. Di sini, warga menyetorkan sampah mereka seperti menabung di bank konvensional, namun yang disetorkan adalah limbah anorganik.\n\nManfaat bank sampah sangat luas. Dari sisi ekonomi, warga bisa mendapatkan penghasilan tambahan atau tabungan yang bisa dicairkan sewaktu-waktu. Dari sisi lingkungan, bank sampah membantu memilah sampah anorganik agar tidak berakhir di TPA, sehingga memudahkan proses daur ulang industri.\n\nSelain itu, bank sampah juga menjadi sarana sosialisasi antar warga untuk peduli lingkungan, menciptakan lingkungan yang lebih bersih, asri, dan terorganisir.",
                url = "https://katadata.co.id/",
                urlToImage = "https://images.unsplash.com/photo-1591193512857-993c4f395874?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-10"
            ),
            Article(
                title = "Cara Membuat Kompos dari Sisa Makanan",
                description = "Pelajari teknik composting sederhana di rumah untuk menyuburkan tanaman.",
                content = "Membuat kompos adalah cara terbaik untuk mengolah sampah organik rumah tangga. Sekitar 50% sampah yang kita hasilkan setiap hari sebenarnya adalah bahan yang bisa dikomposkan, seperti kulit buah, sisa sayur, and ampas kopi.\n\nCaranya cukup mudah: siapkan wadah komposter, masukkan sampah organik yang sudah dipotong kecil, and tambahkan 'aktivator' atau sedikit tanah. Pastikan sirkulasi udara terjaga and kelembapan stabil. Dalam waktu 4-8 minggu, sampah tersebut akan berubah menjadi pupuk alami yang sangat kaya nutrisi bagi tanaman Anda.\n\nDengan membuat kompos, Anda tidak hanya mendapatkan pupuk gratis, tetapi juga secara signifikan mengurangi bau tidak sedap and gas metana yang biasanya muncul dari sampah organik di tempat pembuangan.",
                url = "https://www.greeners.co/",
                urlToImage = "https://images.unsplash.com/photo-1542810634-71277d95dcbb?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-08"
            ),
            Article(
                title = "Bahaya Styrofoam bagi Lingkungan",
                description = "Mengapa kita harus beralih dari wadah sekali pakai styrofoam.",
                content = "Styrofoam atau polystyrene foam adalah salah satu musuh terbesar lingkungan. Meskipun ringan and praktis, bahan ini sangat sulit terurai secara alami, bahkan membutuhkan waktu hingga 500 tahun atau lebih di alam liar.\n\nKetika styrofoam hancur, ia pecah menjadi partikel kecil yang mudah terbang and mencemari saluran air. Selain itu, proses pembuatan styrofoam menghasilkan emisi gas yang merusak lapisan ozon. Dari sisi kesehatan, jika terpapar panas, bahan kimia dalam styrofoam dapat berpindah ke makanan and berisiko memicu gangguan kesehatan.\n\nSolusinya adalah beralih ke wadah makanan yang dapat digunakan berulang kali atau bahan organik yang mudah terurai seperti pelepah pinang atau kertas bersertifikasi ramah lingkungan.",
                url = "https://nationalgeographic.grid.id/",
                urlToImage = "https://images.unsplash.com/photo-1621451537084-482c73073a0f?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-05"
            ),
            Article(
                title = "Panduan Memulai Gaya Hidup Zero Waste",
                description = "Tips bagi pemula untuk mengurangi produksi sampah harian.",
                content = "Memulai gaya hidup zero waste bukan berarti tidak menghasilkan sampah sama sekali, melainkan berusaha meminimalkan limbah yang berakhir di lingkungan. Konsep utamanya adalah 5R: Refuse, Reduce, Reuse, Recycle, and Rot.\n\nLangkah termudah untuk memulai adalah dengan menolak (Refuse) kantong plastik sekali pakai saat belanja. Selalu bawa tas belanja sendiri, sedotan stainless, and botol minum (Tumblr). Langkah selanjutnya adalah mengurangi (Reduce) pembelian barang yang berlebihan.\n\nGaya hidup ini mengajarkan kita untuk lebih sadar akan setiap barang yang kita konsumsi and dampak sampahnya bagi bumi. Ingatlah, tindakan kecil dari banyak orang jauh lebih berdampak daripada satu orang yang melakukannya secara sempurna.",
                url = "https://zerowaste.id/",
                urlToImage = "https://images.unsplash.com/photo-1536939459926-301728717817?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-02"
            ),
            Article(
                title = "Pemanasan Global: Apa yang Bisa Dilakukan?",
                description = "Memahami krisis iklim and langkah kolektif yang bisa kita ambil.",
                content = "Pemanasan global telah menyebabkan perubahan cuaca ekstrem and mencairnya es di kutub. Hal ini terjadi karena meningkatnya konsentrasi gas rumah kaca di atmosfer akibat pembakaran bahan bakar fosil and penggundulan hutan.\n\nKita bisa membantu menekan laju ini dengan menghemat penggunaan energi listrik, beralih ke transportasi rendah emisi, and mendukung upaya konservasi hutan. Selain itu, menyebarkan kesadaran kepada orang di sekitar kita tentang pentingnya menjaga suhu bumi adalah langkah yang sangat berarti.\n\nKrisis iklim adalah tanggung jawab bersama. Meskipun tantangannya besar, setiap upaya untuk hidup lebih hijau akan memberikan nafas bagi bumi yang kita tinggali ini.",
                url = "https://www.bbc.com/indonesia",
                urlToImage = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-03-01"
            ),
            Article(
                title = "Teknologi Hijau: Inovasi Masa Depan",
                description = "Menjelajahi teknologi terbaru yang membantu bumi bernapas lebih lega.",
                content = "Teknologi hijau atau cleantech adalah inovasi yang dirancang untuk mengurangi dampak negatif aktivitas manusia terhadap lingkungan. Saat ini, perkembangan teknologi hijau sangat pesat, mulai dari panel surya yang semakin efisien hingga baterai mobil listrik yang lebih tahan lama.\n\nInovasi lain yang menarik adalah pengolahan air limbah menjadi air bersih siap minum and penggunaan AI untuk mengoptimalkan rute logistik agar hemat bahan bakar. Teknologi ini memberikan harapan bahwa kita bisa terus maju secara ekonomi tanpa harus merusak alam.\n\nInvestasi dalam teknologi hijau adalah investasi untuk masa depan. Dengan mendukung inovasi ini, kita berkontribusi dalam menciptakan dunia yang lebih cerdas and berkelanjutan bagi anak cucu kita kelak.",
                url = "https://id.techinasia.com/",
                urlToImage = "https://images.unsplash.com/photo-1509391366360-fe5bb6583e2c?q=80&w=600&auto=format&fit=crop",
                publishedAt = "2024-02-28"
            )
        )

        val dbRef = db.collection("articles")
        dbRef.get().addOnSuccessListener { snapshot ->
            val batch = db.batch()
            for (doc in snapshot) {
                batch.delete(doc.reference)
            }
            articles.forEach { article ->
                val newDoc = dbRef.document()
                batch.set(newDoc, article)
            }
            batch.commit()
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        }.addOnFailureListener {
            onComplete(false)
        }
    }
}
