package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만들어야함
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManager는 트랜잭션 단위로 만듦
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //code

        // 1. 회원 등록
        try {
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");
//            member.setId(2L);
//            member.setName("HelloB");
            em.persist(member);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // 엔티티매니저가 내부적으로 데이터베이스 커넥션을 물고 동작하여 닫아주는 것이 중요
        }

        // 2. 회원 수정
        try {
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
//            em.persist(findMember); // 이거 안해도 자동으로 저장됨
            // 이유 : JPA를 통해서 엔티티를 가져오면 JPA가 관리함. JPA가 엔티티가 변경되었는지 안됐는지 트랜잭션을 커밋하는 시점에 다 체크하여 바뀌면 업데이트 쿼리를 만들어서 날림.
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        // 2. 회원 삭제
        try {
            Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        // JPQL
        try {
            // 1. 전체 회원 조회
            List<Member> findMembers = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }


        emf.close();
    }
}
