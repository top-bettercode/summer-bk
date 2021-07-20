package top.bettercode.simpleframework.data.test.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import javax.annotation.Generated;


/**
 * QHardUser is a Querydsl query type for HardUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QHardUser extends EntityPathBase<HardUser> {

    private static final long serialVersionUID = 948744413L;

    public static final QHardUser hardUser = new QHardUser("hardUser");

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath firstname = createString("firstname");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath lastname = createString("lastname");

    public QHardUser(String variable) {
        super(HardUser.class, forVariable(variable));
    }

    public QHardUser(Path<? extends HardUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHardUser(PathMetadata metadata) {
        super(HardUser.class, metadata);
    }

}

