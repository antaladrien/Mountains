package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static double atlagSzamitas(List<Hegy> lista) {
        double atlag = 0;
        for (int i = 0; i < lista.size(); i++) {
            atlag += lista.get(i).magassag;
        }
        return atlag / lista.size();
    }

    public static Hegy legmagasabbHegy(List<Hegy> lista) {
        Hegy legmagasabb = lista.get(0);
        for (int i = 0; i < lista.size(); i++) {
            if(legmagasabb.magassag < lista.get(i).magassag) {
                legmagasabb = lista.get(i);
            }
        }
        return legmagasabb;
    }

    public static boolean borzsonybenNagyobb(List<Hegy> lista, int magassag) {
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i).hegyseg.compareTo("Börzsöny") == 0 &&
                lista.get(i).magassag > magassag) {
                return true;
            }
        }
        return false;
    }

    public static int ennyiMagasabbHegyVan(List<Hegy> lista) {
        int db = 0;
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i).magassag > (3000 / 3.280839895)) {
                ++db;
            }
        }
        return db;
    }


    public static HashMap<String, Integer> ennyiDbHegycsucsVan(List<Hegy> lista) {
        HashMap<String, Integer> hegyekDarabSzama =
                new HashMap<>();
        for (int i = 0; i < lista.size(); i++) {
            if(hegyekDarabSzama.containsKey(lista.get(i).hegyseg)) {
                int db = hegyekDarabSzama.get(lista.get(i).hegyseg);
                ++db;
                hegyekDarabSzama.replace(lista.get(i).hegyseg, db);
            } else {
                hegyekDarabSzama.put(lista.get(i).hegyseg, 1);
            }
        }
        return hegyekDarabSzama;
    }


    public static void main(String[] args) {
        String row;
        List<Hegy> hegyek = new ArrayList<>();

        //Fájfeldolgozás
        try (FileReader fr =
                     new FileReader("hegyekMo.txt");
                BufferedReader br = new BufferedReader(fr)) {

            br.readLine();
            while ( (row = br.readLine()) != null ) {
//                System.out.println(row);
                String[] valtozok = row.split(";");
/*
                for (int i = 0; i < valtozok.length; i++) {
                    System.out.println(valtozok[i]);
                }
*/
                String hegycsucs = valtozok[0];
                String hegyseg = valtozok[1];
                int magassag = Integer.parseInt(valtozok[2]);

                Hegy h = new Hegy(hegycsucs, hegyseg, magassag);
                hegyek.add(h);
            }

        } catch (FileNotFoundException fne) {
            System.out.println(fne);
        } catch (IOException ioe) {
            System.out.println(ioe);
        } //Fájlfeldolgozás vége

        //Adatok feldolgozása
        System.out.println("3. feladat: Hegycsúcsok száma: " +
                hegyek.size() + " db");

        System.out.println("4. feladat: Hegycsúcsok átlagos " +
                "magassága: " + atlagSzamitas(hegyek) + " m");

        Hegy legmagasabb = legmagasabbHegy(hegyek);
        System.out.println("5. feladat: A legmagasabb hegycsúcs adatai:");
        System.out.println("Név: " + legmagasabb.hegyCsucs);
        System.out.println("Hegység: " + legmagasabb.hegyseg);
        System.out.println("Magasság: " + legmagasabb.magassag + " m");

        Scanner sc = new Scanner(System.in);
        System.out.println("6. feladat: Kérek egy magasságot: ");
        int ennelNagyobb = sc.nextInt();
        boolean voltEnnelNagyobb = borzsonybenNagyobb(hegyek, ennelNagyobb);

        if(voltEnnelNagyobb) {
            System.out.println("Van " + ennelNagyobb +
                    "m-nél magasabb hegycsúcs a Börzsönyben!");
        } else {
            System.out.println("Nincs " + ennelNagyobb +
                    "m-nél magasabb hegycsúcs a Börzsönyben!");
        }

        int db = ennyiMagasabbHegyVan(hegyek);
        System.out.println("7. feladat: 3000 lábnál magasabb hegycsúcsok száma: " +
                db);



        System.out.println("8. feladat: Hegység statisztika");
        HashMap<String, Integer> h2 = ennyiDbHegycsucsVan(hegyek);
        for (Map.Entry<String, Integer> element : h2.entrySet()) {
            System.out.println(element.getKey() + " - " +
                    element.getValue() + " db");
        }


        try (FileWriter fw = new FileWriter("bukk-videk.txt");
            BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write("Hegycsúcs neve;Magasság láb");
            bw.newLine();
            for (int i = 0; i < hegyek.size(); i++) {
                Hegy h = hegyek.get(i);
                double lab = h.magassag * 3.28;
                String konvertalt = String.format("%.1f", lab);
                konvertalt = konvertalt.replace(
                        ",", ".");
                if(konvertalt.endsWith(".0")) {
                    konvertalt = konvertalt.replace(
                            ".0", "");
                }
                if(h.hegyseg.compareTo("Bükk-vidék") == 0) {
                    bw.write(h.hegyCsucs + ";" +
                             konvertalt);
                    bw.newLine();
                }
            }

        } catch (FileNotFoundException fne) {
            System.out.println(fne);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        System.out.println("9. feladat: bukk-videk.txt");
    }
}
