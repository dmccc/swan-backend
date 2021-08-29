package io.github.myifeng.swan.demo.controller;

import io.github.myifeng.swan.demo.dao.DemoDao;
import io.github.myifeng.swan.demo.entity.DemoEntity;
import io.github.myifeng.swan.starter.auth.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class DemoController {

    private final DemoDao dao;

    @Autowired
    public DemoController(DemoDao dao) {
        this.dao = dao;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<DemoEntity> page(@RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "rows", defaultValue = "20") int rows) {
        return dao.findAllBy(PageRequest.of(page, rows), DemoEntity.class);
    }

    @Transactional
    @PostMapping
    public DemoEntity create(@RequestBody DemoEntity demo, AccountDetails details) {
        demo.setCreateUser(details.getId());
        return dao.saveAndFlush(demo);
    }

    @Transactional
    @PutMapping("/{id}")
    public DemoEntity update(@RequestBody DemoEntity demo, @PathVariable Long id) {
        demo.setId(id);
        return dao.saveAndFlush(demo);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dao.deleteById(id);
    }

    @GetMapping("/{id}")
    public DemoEntity getById(@PathVariable Long id) {
        return dao.findById(id).orElse(null);
    }
}
