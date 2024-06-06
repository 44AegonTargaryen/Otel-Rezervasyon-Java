### Proje Raporu: Otel Rezervasyon Sistemi

#### 1. Projenin Amacı, Nasıl Yapıldığı ve Karşılaşılan Sorunlar

**Projenin Amacı:**
Bu proje, bir sanal otel için rezervasyon sistemi oluşturmayı amaçlamaktadır. Kullanıcılar oteldeki odaları rezerve edebilir, rezervasyonları iptal edebilir ve otel doluluk raporlarını görüntüleyebilir. Proje, otel yönetiminin rezervasyon işlemlerini ve raporlama süreçlerini daha verimli hale getirmek için tasarlanmıştır.

**Nasıl Yapıldı:**
Proje, Java programlama dili kullanılarak ve Swing kütüphanesi ile kullanıcı arayüzü (GUI) oluşturularak geliştirildi. Proje, SQL Server veritabanı ile entegre edilerek oda ve rezervasyon bilgilerini yönetmek için gerekli veri tabanı işlemlerini gerçekleştirdi.

- **Oda Yönetimi ve Rezervasyon Sistemi:**
  - `Room`, `HotelInfo`, `HotelManager`, `BookingScreen`, `ReservationScreen`, `MainScreen` sınıfları oluşturuldu.
  - Oda bilgileri, rezervasyon işlemleri ve iptal işlemleri bu sınıflar aracılığıyla yönetildi.

- **Zamanlayıcı İşlevleri:**
  - Her 24 saniyede bir otel doluluk raporu oluşturan bir zamanlayıcı (`java.util.Timer` ve `java.util.TimerTask`) eklendi.
  - Rapor, dolu odaların sayısını ve bu odalardan elde edilen toplam geliri gösterir.

- **Veritabanı Bağlantısı:**
  - SQL Server veritabanına bağlantı kurularak oda bilgileri ve rezervasyonlar yönetildi.
  - JDBC kullanılarak veritabanı işlemleri gerçekleştirildi.

- **Kullanıcı Arayüzü:**
  - Swing kullanılarak kullanıcı arayüzü oluşturuldu.
  - Kullanıcılar arayüz üzerinden odaları görüntüleyebilir, rezervasyon yapabilir, rezervasyonları iptal edebilir ve raporları görüntüleyebilir.

**Karşılaşılan Sorunlar ve Çözümleri:**
- **Veritabanı Bağlantı Sorunları:**
  - JDBC sürücüsü eksikliği nedeniyle bağlantı sorunları yaşandı. Bu sorun, JDBC sürücüsünün projeye eklenmesiyle çözüldü.
- **Zamanlayıcı İşlevleri:**
  - Zamanlayıcı işlevlerinin doğru çalışmaması. Bu sorun, `java.util.Timer` sınıfının doğru kullanımı ve periyotların doğru ayarlanması ile çözüldü.
- **GUI Güncellemeleri:**
  - Kullanıcı arayüzünün doğru güncellenmemesi. Bu sorun, arayüz bileşenlerinin doğru yeniden çizilmesi (`revalidate`, `repaint`) ile çözüldü.

#### 2. Geliştirme Ortamı

- **Programlama Dili:** Java
- **Geliştirme Ortamı (IDE):** IntelliJ IDEA (Community Edition)
- **Java Development Kit (JDK):** JDK 16.0.2
- **Veritabanı Yönetim Sistemi (DBMS):** Microsoft SQL Server 2019
- **JDBC Sürücüsü:** mssql-jdbc-9.2.1.jre8.jar

#### 3. Veri Tabanı Tabloları ve Normalizasyon

**Veri Tabanı Tabloları:**
- **hotel_info**
  - `name` (VARCHAR)
  - `location` (VARCHAR)
  - `room_count` (INT)
  - `daily_rate` (FLOAT)

- **rooms**
  - `room_number` (INT, PRIMARY KEY)
  - `room_type` (VARCHAR)
  - `price` (FLOAT)
  - `is_reserved` (BIT)
  - `customer_name` (VARCHAR)

**Normalizasyon:**
Veri tabanı tabloları 3. Normal Forma (3NF) uygun olarak tasarlanmıştır. Her tablo, belirli bir veri kümesini ve ilgili bağımsız değişkenleri tutar.

#### 4. Veri Tabanı İçin Kullanılan SQL Kodları ve Anlamları

**Tabloların Oluşturulması:**

```sql
CREATE TABLE hotel_info (
    name VARCHAR(255),
    location VARCHAR(255),
    room_count INT,
    daily_rate FLOAT
);

CREATE TABLE rooms (
    room_number INT PRIMARY KEY,
    room_type VARCHAR(255),
    price FLOAT,
    is_reserved BIT,
    customer_name VARCHAR(255)
);
```

Bu SQL kodları, `hotel_info` ve `rooms` tablolarını oluşturur.

**Veritabanı İşlemleri:**

**Oda Bilgilerini Yükleme:**
```java
try (Connection connection = DatabaseConnection.getConnection()) {
    String sql = "SELECT room_number, room_type, price, is_reserved, customer_name FROM rooms";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {
        while (resultSet.next()) {
            int roomNumber = resultSet.getInt("room_number");
            RoomType roomType = RoomType.valueOf(resultSet.getString("room_type"));
            double price = resultSet.getDouble("price");
            boolean isReserved = resultSet.getBoolean("is_reserved");
            String customerName = resultSet.getString("customer_name");
            rooms.add(new Room(roomNumber, roomType, price, isReserved, customerName));
        }
    }
}
```

Bu Java kodu, `rooms` tablosundaki oda bilgilerini yükler ve `Room` nesnelerine dönüştürür.

**Rezervasyon Yapma:**
```java
String sql = "UPDATE rooms SET is_reserved = 1, customer_name = ? WHERE room_number = ?";
try (PreparedStatement statement = connection.prepareStatement(sql)) {
    statement.setString(1, customerName);
    statement.setInt(2, roomNumber);
    statement.executeUpdate();
}
```

Bu SQL kodu, belirtilen oda numarasını rezerve eder ve müşteri adını ekler.

**Rezervasyon İptal Etme:**
```java
String sql = "UPDATE rooms SET is_reserved = 0, customer_name = NULL WHERE room_number = ?";
try (PreparedStatement statement = connection.prepareStatement(sql)) {
    statement.setInt(1, roomNumber);
    statement.executeUpdate();
}
```

Bu SQL kodu, belirtilen oda numarasının rezervasyonunu iptal eder ve müşteri adını temizler.

### Projeyi Çalıştırma

Projeyi çalıştırmak için aşağıdaki adımları takip edebilirsiniz:

1. **Veritabanı Bağlantısı:** 
   - `DatabaseConnection` sınıfında veritabanı bağlantı bilgilerinizi güncelleyin.
   - JDBC sürücüsünü projeye ekleyin.

2. **Projeyi Çalıştırma:**
   - IntelliJ IDEA veya tercih ettiğiniz IDE'de projeyi açın.
   - `MainScreen` sınıfını çalıştırarak uygulamayı başlatın.

### Proje Dosyaları ve Klasör Yapısı

Projeyi GitHub'a yüklediğinizde, klasör yapısı aşağıdaki gibi olmalıdır:

```
/project-root
    /src
        /com
            /hotel
                BookingScreen.java
                DatabaseConnection.java
                HotelInfo.java
                HotelManager.java
                LoginScreen.java
                MainScreen.java
                ReservationScreen.java
                Room.java
                RoomType.java
    /lib
        mssql-jdbc-9.2.1.jre8.jar
    create_and_insert_tables.sql
    README.md
```

### README.md Dosyası

Projenizi anlamayan bir kişinin projeyi çalıştırabilmesi için bir `README.md` dosyası ekleyin. Aşağıdaki örneği kullanabilirsiniz:

```markdown
# Hotel Reservation System

## Proje Açıklaması
Bu proje, bir sanal otel için rezervasyon sistemi oluşturmayı amaçlamaktadır. Kullanıcılar oteldeki odaları rezerve edebilir, rezervasyonları iptal edebilir ve otel doluluk raporlarını görüntüleyebilir.

## Gereksinimler
- Java JDK 16.0.2
- IntelliJ IDEA (Community Edition)
- Microsoft SQL Server 2019
- JDBC Driver for SQL Server

## Kurulum Adımları

### 1. Projeyi Klonlayın
```bash
git clone https://github.com/44AegonTargaryen/Otel-Rezervasyon-Java
cd Otel-Rezervasyon-Java
```

### 2. Veritabanını Kurun
- SQL Server Management Studio'yu açın.
- `create_and_insert_tables.sql` dosyasını çalıştırarak veritabanı tablolarını ve başlangıç verilerini oluşturun.

### 3. JDBC Sürücüsünü Ekleyin
- `lib/mssql-jdbc-9.2.1.jre8.jar` dosyasını projenize ekleyin (IntelliJ IDEA'da File -> Project Structure -> Modules -> Dependencies kısmından ekleyebilirsiniz).

### 4. Veritabanı Bağlantısını Güncelleyin
- `DatabaseConnection.java` dosyasındaki veritabanı bağlantı bilgilerini kendi SQL Server bilgilerinize göre güncelleyin.

### 5. Uygulamayı Çalıştırın
- `MainScreen` sınıf

ını çalıştırarak uygulamayı başlatın.

## Proje Yapısı
- `src/com/hotel` dizininde tüm Java kaynak kodları bulunmaktadır.
- `lib` dizininde JDBC sürücüsü bulunmaktadır.
- `create_and_insert_tables.sql` dosyası veritabanı tablolarını ve başlangıç verilerini oluşturur.
