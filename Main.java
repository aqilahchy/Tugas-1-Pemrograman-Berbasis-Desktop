import java.util.Scanner;

public class Main {
    static Menu[] daftarMenu = {
        new Menu("Nasi Ayam Madura", 20000, "makanan"),
        new Menu("Ketoprak", 15000, "makanan"),
        new Menu("Nasi Bebek Madura", 21000, "makanan"),
        new Menu("Nasi Goreng", 17000, "makanan"),
        new Menu("Mie Ayam", 14000, "makanan"),
        new Menu("Es Teh Manis", 5000, "minuman"),
        new Menu("Jus Alpukat", 15000, "minuman"),
        new Menu("Es Jeruk", 7000, "minuman"),
        new Menu("Es Teh Tawar", 4500, "minuman"),
        new Menu("Air Mineral", 5000, "minuman")
    };
    
    static String[] namaPesanan = new String[4];
    static int[] jumlahPesan = new int[4];
    static double[] hargaPesan = new double[4];
    static int jumlahItem = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        tampilkanMenu();
        inputPesanan(sc);
        double total = hitungTotal();
        cetakStruk(total);
        sc.close();
    }

    static void tampilkanMenu() {
        System.out.println("========== DAFTAR MENU ==========");
        System.out.printf("%-3s %-20s %-10s %-10s\n", "No", "Menu", "Harga", "Kategori");
        for (int i = 0; i < daftarMenu.length; i++) {
            System.out.printf("%-3d %-20s Rp %,8.0f %-10s\n", i+1, daftarMenu[i].getNama(), 
                daftarMenu[i].getHarga(), daftarMenu[i].getKategori());
        }
        System.out.println("=================================");
    }

    static void inputPesanan(Scanner sc) {
        System.out.println("\nMaksimal 4 item pesanan. Tulis 'selesai' untuk selesai.");
        while (jumlahItem < 4) {
            System.out.print("Pesanan " + (jumlahItem+1) + " : ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("selesai")) break;
            
            String[] parts = input.split("=");
            if (parts.length != 2) {
                System.out.println("[!] Format: <Nama Menu> = <Jumlah>");
                continue;
            }
            
            String nama = parts[0].trim();
            int jumlah;
            try {
                jumlah = Integer.parseInt(parts[1].trim());
            } catch (Exception e) {
                System.out.println("[!] Jumlah harus angka");
                continue;
            }
            
            Menu menu = cariMenu(nama);
            if (menu == null) {
                System.out.println("[!] Menu tidak ditemukan");
                continue;
            }
            
            if (jumlah <= 0) {
                System.out.println("[!] Jumlah harus lebih dari 0");
                continue;
            }
            
            namaPesanan[jumlahItem] = menu.getNama();
            jumlahPesan[jumlahItem] = jumlah;
            hargaPesan[jumlahItem] = menu.getHarga();
            System.out.println("[✓] Ditambahkan: " + menu.getNama() + " x" + jumlah);
            jumlahItem++;
        }
    }

    static Menu cariMenu(String nama) {
        for (Menu m : daftarMenu) {
            if (m.getNama().equalsIgnoreCase(nama)) {
                return m;
            }
        }
        return null;
    }

    static double hitungTotal() {
        double total = 0;
        for (int i = 0; i < jumlahItem; i++) {
            total += hargaPesan[i] * jumlahPesan[i];
        }
        return total;
    }

    static void cetakStruk(double totalSebelumDiskon) {
        System.out.println("\n============== STRUK ==============");
        System.out.printf("%-18s %4s %10s %10s\n", "Menu", "Jml", "Harga", "Subtotal");
        
        for (int i = 0; i < jumlahItem; i++) {
            double subtotal = hargaPesan[i] * jumlahPesan[i];
            System.out.printf("%-18s %4d Rp %,8.0f Rp %,8.0f\n",
                namaPesanan[i], jumlahPesan[i], hargaPesan[i], subtotal);
        }
        
        System.out.println("-----------------------------------");
        System.out.printf("%-32s Rp %,8.0f\n", "Total Sebelum Diskon:", totalSebelumDiskon);
        
        double diskon = 0;
        if (totalSebelumDiskon > 100000) {
            diskon = totalSebelumDiskon * 0.10;
            System.out.printf("%-32s -Rp %,8.0f\n", "Diskon 10%:", diskon);
        }
        
        double minumanGratis = 0;
        if (totalSebelumDiskon > 50000) {
            minumanGratis = cariHargaMinumanGratis();
            if (minumanGratis > 0) {
                System.out.printf("%-32s -Rp %,8.0f\n", "Gratis 1x Minuman:", minumanGratis);
            }
        }
        
        double totalSetelahDiskon = totalSebelumDiskon - diskon - minumanGratis;
        double pajak = totalSetelahDiskon * 0.10;
        double pelayanan = 20000;
        double totalAkhir = totalSetelahDiskon + pajak + pelayanan;
        
        System.out.printf("%-32s Rp %,8.0f\n", "Pajak (10%):", pajak);
        System.out.printf("%-32s Rp %,8.0f\n", "Biaya Pelayanan:", pelayanan);
        System.out.println("===================================");
        System.out.printf("%-32s Rp %,8.0f\n", "TOTAL AKHIR:", totalAkhir);
        System.out.println("Terima kasih!");
    }

    static double cariHargaMinumanGratis() {
        for (int i = 0; i < jumlahItem; i++) {
            Menu m = cariMenu(namaPesanan[i]);
            if (m != null && m.getKategori().equalsIgnoreCase("minuman")) {
                return m.getHarga();
            }
        }
        return 0;
    }
}