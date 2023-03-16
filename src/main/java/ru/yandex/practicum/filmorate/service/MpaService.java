package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

@Slf4j
@Service
public class MpaService extends AbstractService<Mpa> {
    public MpaService(@Qualifier("MpaDbStorage") MpaDbStorage mpaDbStorage) {
        super(mpaDbStorage);
    }
}