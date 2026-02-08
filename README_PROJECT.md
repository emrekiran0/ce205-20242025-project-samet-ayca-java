# Legal Case Tracker - Hukuki Dava Takip Sistemi

![Java](https://img.shields.io/badge/Java-8+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-GPL%20v3-blue?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Windows%20|%20macOS%20|%20Linux-lightgrey?style=for-the-badge)

**Gelismis veri yapilari ve algoritmalar kullanilarak gelistirilmis kapsamli bir hukuki dava yonetim sistemi**

---

## Proje Hakkinda

**Legal Case Tracker**, avukatlar ve hukuk burolarinin dava sureclerini etkin bir sekilde yonetmelerini saglayan konsol tabanli bir Java uygulamasidir.

Bu proje, **Recep Tayyip Erdogan Universitesi Bilgisayar Muhendisligi** bolumunde CE205 - Data Structures dersi kapsaminda gelistirilmistir.

### Projenin Amaci

- Hukuki davalarin sistematik takibi ve yonetimi
- Davaci-davali iliskilerinin graf yapisi ile modellenmesi
- Guvenli kullanici kimlik dogrulama sistemi
- Verimli veri arama ve erisim mekanizmalari

---

## Ozellikler

### Temel Islevler

| Ozellik | Aciklama |
|---------|----------|
| **Dava Yonetimi** | Dava olusturma, guncelleme, silme ve arama islemleri |
| **Taraf Yonetimi** | Davaci ve davali bilgilerini yonetme |
| **Tarih Planlama** | Durusma tarihlerinin otomatik planlanmasi |
| **Guvenli Giris** | Huffman kodlamasi ile sifrelenmis kimlik dogrulama |
| **Geri Alma** | Silinen davalari Stack yapisi ile geri yukleme |
| **Kalici Veri** | Dosya bazli veri saklama ve seri hale getirme |

### Gelismis Ozellikler

- **Hizli Arama**: Hash tablosu ve B+ Tree ile O(1) - O(log n) erisim
- **Iliski Analizi**: Graf algoritmalari ile taraf iliskileri analizi
- **Veri Sikistirma**: Huffman kodlamasi ile guvenli veri saklama
- **Zamanlama Optimizasyonu**: Sparse Matrix ile durusma planlamasi

---

## Veri Yapilari ve Algoritmalar

| Veri Yapisi | Kullanim Alani | Zaman Karmasikligi |
|-------------|----------------|-------------------|
| **B+ Tree** | Dava kayitlarinin sirali saklanmasi | O(log n) |
| **Hash Table** | Dava ID ile hizli erisim (Quadratic Probing, Double Hashing) | O(1) ortalama |
| **Huffman Tree** | Sifre guvenligi icin karakter kodlama | O(n log n) |
| **Graph (Adjacency List)** | Davaci-davali iliski agi | O(V + E) |
| **Stack** | Silinen davalarin geri alinmasi (Undo) | O(1) |
| **Min Heap** | Oncelik kuyrugu islemleri | O(log n) |
| **Sparse Matrix** | Durusma takvimi yonetimi | O(1) erisim |
| **Custom Queue** | Islem sirasi yonetimi | O(1) |

### Algoritma Kullanimlari

- **Quadratic Probing**: Hash cakisma cozumu
- **Double Hashing**: Alternatif cakisma cozumu
- **Progressive Overflow**: Hash tasma yonetimi
- **Huffman Encoding**: Veri sikistirma ve sifreleme
- **Graph Traversal**: Iliski agi analizi

---

## Kurulum

### Gereksinimler

- **Java JDK 8+**
- **Apache Maven 3.6+**
- **Git**

### Hizli Baslangic

```bash
# Repository klonlayin
git clone https://github.com/emrekiran0/ce205-20242025-project-samet-ayca-java.git

# Proje dizinine gidin
cd ce205-20242025-project-samet-ayca-java/legalcase-app

# Projeyi derleyin
mvn clean install

# Uygulamayi calistirin
java -jar target/legalcase-app-1.0-SNAPSHOT.jar
```

---

## Test ve Kalite

- GitHub Actions ile otomatik build ve test
- Multi-platform destegi (Windows, macOS, Linux)
- JaCoCo ile kod kapsama raporlari
- Release otomasyonu

---

## Ekip

| Gelistirici | Rol |
|------------|-----|
| **Arda FIRIDIN** | Developer & Designer |
| **Emre KIRAN** | Developer |
| **Samet AYCA** | Developer |
| **Erdem Bekir AKTURK** | Developer |

**Kurum:** Recep Tayyip Erdogan Universitesi - Bilgisayar Muhendisligi Bolumu

**Akademik Yil:** 2024-2025

---

## Teknoloji Yigini

- Java 8+
- Apache Maven
- JUnit 4
- JaCoCo
- GitHub Actions
- Doxygen
- SLF4J / Logback

---

## Ogrenilen Beceriler

- Gelismis veri yapilarinin tasarimi ve implementasyonu
- Hash tablosu cakisma cozum teknikleri
- Agac tabanli veri yapilari (B+ Tree, Huffman Tree)
- Graf algoritmalari ve iliski modelleme
- Nesne yonelimli programlama prensipleri
- Maven ile proje yonetimi
- CI/CD pipeline kurulumu
- Birim test yazimi ve kod kapsama analizi

---

## Lisans

Bu proje GNU General Public License v3.0 altinda lisanslanmistir.

---

**Projeyi begendiseniz yildiz vermeyi unutmayin!**

Developed at RTEU Computer Engineering Department
