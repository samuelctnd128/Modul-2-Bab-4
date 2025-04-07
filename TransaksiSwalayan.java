import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Pelanggan {
    private String nomorPelanggan;
    private  String nama;
    private double saldo;
    private String pin;
    private int kesalahanAutentikasi;
    private boolean diblokir;

    // Constructor dari variabel-variabel yang diperlukan
    public Pelanggan(String nomorPelanggan, String nama, double saldo, String pin) {
        this.nomorPelanggan = nomorPelanggan;
        this.nama = nama;
        this.saldo = saldo;
        this.pin = pin;
        this.kesalahanAutentikasi = 0;
        this.diblokir = false;
    }

    // Method untuk mengembalikan variabel nama
    public String getNama() {
        return nama;
    }

    // Method untuk mengembalikan variabel saldo
    public double getSaldo() {
        return saldo;
    }

    // Method untuk mendapatkan variabel jenis pelanggan
    public String getJenis() {
    String kodeJenis = nomorPelanggan.substring(0, 2);
    String Jenis = null;
        switch(kodeJenis) {
            case "38": Jenis = "Silver"; break;
            case "56": Jenis = "Gold"; break;
            case "74": Jenis = "Platinum"; break;
        } 
        return Jenis;
    }

    // Method autentikasi PIN
    public boolean autentikasi(String pin) {
        // Memunculkan pesan jika akun diblokir
        if (diblokir) {
            System.out.println("Akun diblokir!");
            return false;
        }
        // Nilai awal dari kesalahan memasukkan PIN
        // Nilai bertambah jika PIN yang dimasukkan salah
        if (this.pin.equals(pin)) {
            kesalahanAutentikasi = 0;
            return true;
        } else {
            kesalahanAutentikasi++;
            // Kode jika setelah 3 kali salah memasukkan PIN maka akan memblokir akun
            if (kesalahanAutentikasi >= 3) {
                diblokir = true;
                System.out.println("Akun diblokir karena terlalu banyak kesalahan input PIN!");
            }
            return false;
        }
    }

    // Method top-up saldo dengan menjumlahkan saldo dengan nilai yang diinput (jumlah)
    public void topUp(double jumlah) {
        saldo += jumlah;
        System.out.printf("Top up berhasil! Saldo saat ini: Rp%.0f%n", saldo);
    }

    // Method untuk pembelian
    public boolean beli(double jumlah) {
        // Jika saldo kurang dari 10000, maka muncul pesan transaksi gagal
        if (saldo - jumlah < 10000) {
            System.out.println("Transaksi gagal! Saldo tidak mencukupi.");
            return false;
        }
        // Kode untuk menambahkan jumlah cashback
        double cashback = hitungCashback(jumlah);
        saldo -= jumlah;
        saldo += cashback;
        System.out.printf("Pembelian berhasil! Saldo saat ini: Rp%.0f%n", saldo);
        return true;
    }
    // Method hitung cashback
    private double hitungCashback(double jumlah) {
        String kodeJenis = nomorPelanggan.substring(0, 2);
        double cashback = 0;

        if (jumlah > 1000000) {
            switch (kodeJenis) {
                // Pelanggan jenis silver; setiap pembelian diatas 1 jt maka mendapat cashback sebesar 5%
                case "38": cashback = jumlah * 0.05; break;
                // Pelanggan jenis gold; setiap pembelian diatas 1 jt maka mendapat cashback sebesar 7%
                case "56": cashback = jumlah * 0.07; break;
                // Pelanggan jenis platinum; setiap pembelian diatas 1 jt maka mendapat cashback sebesar 10%
                case "74": cashback = jumlah * 0.10; break;
            }
        } else {
            switch (kodeJenis) {
                // Pelanggan jenis gold; setiap pembelian dibawah 1 jt maka mendapat cashback sebesar 2%
                case "56": cashback = jumlah * 0.02; break;
                // Pelanggan jenis platinum; setiap pembelian dibawah 1 jt maka mendapat cashback sebesar 5%
                case "74": cashback = jumlah * 0.05; break;
            }
        }
        System.out.printf("Cashback didapat: Rp%.0f%n", cashback);
        return cashback;
    }
}

// Class utama transaksi swalayan
public class TransaksiSwalayan {
    // Membuat database hashMap baru
    private static Map<String, Pelanggan> databasePelanggan = new HashMap<>();
    // Deklarasi Scanner
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Database dari para pelanggan yang terdiri dari nomor pelanggan, nama pelanggan, saldo, dan PIN
        databasePelanggan.put("3898626566", new Pelanggan("3898626566", "Paijo", 5000, "1864"));
        databasePelanggan.put("5615186974", new Pelanggan("5615186974", "Eddie", 1250000, "3681"));
        databasePelanggan.put("7472498219", new Pelanggan("7472498219", "Kirby", 2200000, "7438"));

        // Perintah memasukkan data pelanggan
        while (true) {
            System.out.println("Selamat datang di Swalayan Tiny!");
            System.out.print("Masukkan Nomor Pelanggan: ");
            // Masukkan nomor pelanggan
            String nomor = input.nextLine();
            
             // Method jika nomor pelanggan tidak valid/tidak ada
            Pelanggan pelanggan = databasePelanggan.get(nomor);
            if (pelanggan == null) {
                System.out.println("Nomor pelanggan tidak ditemukan!");
                continue;
            }

            // Perintah untuk memasukkan PIN
            System.out.print("Masukkan PIN: ");
            String pin = input.nextLine();
            if (!pelanggan.autentikasi(pin)) {
                continue;
            }

            // Menampilkan nama pelanggan, jenis pelanggan, dan saldo pelanggan
            System.out.println("Halo, " + pelanggan.getNama() + "!");
            System.out.println("Anda adalah pelanggan " + pelanggan.getJenis() + ".");
            System.out.printf("Saldo anda adalah Rp%.0f%n", pelanggan.getSaldo());
            // Memilih opsi untuk membeli, top-up, dan keluar
            System.out.println("1. Pembelian\n2. Top Up\n3. Keluar");
            System.out.print("Pilih opsi: ");
            int opsi = input.nextInt();
            input.nextLine();

            switch (opsi) {
                case 1:
                // Opsi pembelian
                    System.out.print("Masukkan jumlah pembelian: ");
                    int jumlah = input.nextInt();
                    pelanggan.beli(jumlah);
                    break;
                case 2:
                // Opsi top-up
                    System.out.print("Masukkan jumlah top up: ");
                    int topUp = input.nextInt();
                    pelanggan.topUp(topUp);
                    break;
                case 3:
                // Opsi keluar
                    System.out.println("Terima kasih telah menggunakan layanan kami!");
                    return;
                // Opsi tidak valid
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}