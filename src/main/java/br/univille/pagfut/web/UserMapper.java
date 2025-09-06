package br.univille.pagfut.web;

import br.univille.pagfut.api.user.UserRequest;
import br.univille.pagfut.api.user.UserResponse;
import br.univille.pagfut.domain.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity (UserRequest userRequest);

    UserResponse toDto (UserEntity userEntity);
}
