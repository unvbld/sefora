package com.sefora.minimarket.service;

import com.sefora.minimarket.model.Transaksi;
import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.model.Keranjang;
import com.sefora.minimarket.repository.TransaksiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private KeranjangService keranjangService;

    public void createTransaksi(Pembeli pembeli) {
        List<Keranjang> keranjangs = keranjangService.getKeranjangByPembeli(pembeli);

        // Menggunakan BigDecimal untuk jumlah
        BigDecimal totalHarga = keranjangs.stream()
                .map(keranjang -> keranjang.getBarang().getHarga()
                        .multiply(BigDecimal.valueOf(keranjang.getJumlah())))  // Mengkonversi jumlah menjadi BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Transaksi transaksi = new Transaksi();
        transaksi.setPembeli(pembeli);
        transaksi.setTotalHarga(totalHarga);
        transaksi.setTanggalTransaksi(LocalDateTime.now());

        transaksiRepository.save(transaksi);

        // Hapus barang dari keranjang setelah checkout
        keranjangs.forEach(keranjang -> keranjangService.deleteBarangFromKeranjang(keranjang.getId()));
    }

    public void saveTransaksi(Transaksi transaksi) {
        transaksiRepository.save(transaksi);
    }
}
