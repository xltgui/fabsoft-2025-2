package br.univille.pagfut.web;

import br.univille.pagfut.api.user.UserRequest;
import br.univille.pagfut.api.user.UserResponse;
import br.univille.pagfut.domain.user.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-26T18:12:38-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntity(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername( userRequest.username() );
        userEntity.setPassword( userRequest.password() );

        return userEntity;
    }

    @Override
    public UserResponse toDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String password = null;

        id = userEntity.getId();
        username = userEntity.getUsername();
        password = userEntity.getPassword();

        UserResponse userResponse = new UserResponse( id, username, password );

        return userResponse;
    }
}
