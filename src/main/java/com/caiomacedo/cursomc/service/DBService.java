package com.caiomacedo.cursomc.service;

import com.caiomacedo.cursomc.domain.*;
import com.caiomacedo.cursomc.domain.enums.ClientType;
import com.caiomacedo.cursomc.domain.enums.PaymentStatus;
import com.caiomacedo.cursomc.domain.enums.Profile;
import com.caiomacedo.cursomc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private CategoryRepository cr;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StateRepository sr;

    @Autowired
    private CityRepository cir;

    @Autowired
    private AddressRepository ar;

    @Autowired
    private ClientRepository clr;

    @Autowired
    private OrderRepository or;

    @Autowired
    private PaymentRepository payr;

    @Autowired
    private OrderItemRepository oir;

    public void instantiateTestDatabase() throws ParseException {
        Category cat1 = new Category(null, "Informática");
        Category cat2 = new Category(null, "Escritório");
        Category cat3 = new Category(null, "Cama,mesa e banho");
        Category cat4 = new Category(null, "Eletrônicos");
        Category cat5 = new Category(null, "Jardinagem");
        Category cat6 = new Category(null, "Decoração");
        Category cat7 = new Category(null, "Perfumaria");


        Product p1 = new Product(null, "Computador", 2000.00);
        Product p2 = new Product(null, "Impressora", 800.00);
        Product p3 = new Product(null, "Mouse", 80.00);
        Product p4 = new Product(null, "Mesa de escritório", 300.00);
        Product p5 = new Product(null, "Toalha", 50.00);
        Product p6 = new Product(null, "Coalha", 200.00);
        Product p7 = new Product(null, "TV true color", 1200.00);
        Product p8 = new Product(null, "Roçadeira", 800.00);
        Product p9 = new Product(null, "Abajour", 100.00);
        Product p10 = new Product(null, "Pendente", 180.00);
        Product p11 = new Product(null, "Shampoo", 90.00);


        cat1.getList().addAll(Arrays.asList(p1, p2, p3));
        cat2.getList().addAll(Arrays.asList(p2));
        cat3.getList().addAll(Arrays.asList(p5, p6));
        cat4.getList().addAll(Arrays.asList(p1, p2, p3, p7));
        cat5.getList().addAll(Arrays.asList(p8));
        cat6.getList().addAll(Arrays.asList(p9, p10));
        cat7.getList().addAll(Arrays.asList(p11));


        p1.getCategories().addAll(Arrays.asList(cat1, cat4));
        p2.getCategories().addAll(Arrays.asList(cat1, cat2, cat4));
        p3.getCategories().addAll(Arrays.asList(cat1, cat4));
        p4.getCategories().addAll(Arrays.asList(cat2));
        p5.getCategories().addAll(Arrays.asList(cat3));
        p6.getCategories().addAll(Arrays.asList(cat3));
        p7.getCategories().addAll(Arrays.asList(cat4));
        p8.getCategories().addAll(Arrays.asList(cat5));
        p9.getCategories().addAll(Arrays.asList(cat6));
        p10.getCategories().addAll(Arrays.asList(cat6));
        p11.getCategories().addAll(Arrays.asList(cat7));


        cr.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
        productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));

        State est1 = new State(null, "Minas Gerais");
        State est2 = new State(null, "São Paulo");

        City c1 = new City(null, "Uberlândia", est1);
        City c2 = new City(null, "São Paulo", est2);
        City c3 = new City(null, "Campinas", est2);

        est1.getCities().addAll(Arrays.asList(c1));
        est2.getCities().addAll(Arrays.asList(c2, c3));

        sr.saveAll(Arrays.asList(est1, est2));
        cir.saveAll(Arrays.asList(c1, c2, c3));

        Client cli1 = new Client(null, "Caio Macedo", "ccaiogatao@gmail.com", "66605402022", ClientType.PESSOAFISICA,pe.encode("123"));
        cli1.getTelefone().addAll(Arrays.asList("434242423", "42423424"));

        Client cli2 = new Client(null, "Ana costa", "ccaiovictor@hotmail.com", "28403689004", ClientType.PESSOAFISICA,pe.encode("123"));
        cli2.getTelefone().addAll(Arrays.asList("989242423", "757575424"));
        cli2.addPerfil(Profile.ADMIN);

        Address e1 = new Address(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);
        Address e2 = new Address(null, "Avenida Matos", "105", "Sala 800", "Centro", "4234242", cli1, c2);
        Address e3 = new Address(null, "Avenida Floriano", "2106", null, "Centro", "6454242", cli2, c2);

        cli1.getAddress().addAll(Arrays.asList(e1, e2));
        cli2.getAddress().addAll(Arrays.asList(e3));

        clr.saveAll(Arrays.asList(cli1,cli2));
        ar.saveAll(Arrays.asList(e1, e2,e3));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Orders ped1 = new Orders(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
        Orders ped2 = new Orders(null, sdf.parse("10/10/2017 19:35"), cli1, e2);

        Payment pagto1 = new CardPayment(null, PaymentStatus.QUITADO, ped1, 6);
        ped1.setPagamento(pagto1);
        Payment pagto2 = new BilletPayment(null, PaymentStatus.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
        ped2.setPagamento(pagto2);

        cli1.getPedidos().addAll(Arrays.asList(ped1, ped2)); //associação do cliente com os pedidos

        or.saveAll(Arrays.asList(ped1, ped2));
        payr.saveAll(Arrays.asList(pagto1, pagto2));

        OrderItem ip1 = new OrderItem(ped1, p1, 0.00, 1, 2000.00);
        OrderItem ip2 = new OrderItem(ped1, p3, 0.00, 2, 80.00);
        OrderItem ip3 = new OrderItem(ped2, p2, 100.00, 1, 800.00);

        ped1.getItens().addAll(Arrays.asList(ip1, ip2));
        ped2.getItens().addAll(Arrays.asList(ip3));

        p1.getItens().addAll(Arrays.asList(ip1));
        p2.getItens().addAll(Arrays.asList(ip3));
        p3.getItens().addAll(Arrays.asList(ip2));

        oir.saveAll(Arrays.asList(ip1, ip2, ip3));

    }


}
