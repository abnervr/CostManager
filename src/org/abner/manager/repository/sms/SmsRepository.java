package org.abner.manager.repository.sms;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.sms.Sms;
import org.abner.manager.repository.Repository;

public interface SmsRepository extends Repository<Sms> {

    Sms findLastSms(String... senders);

    Sms findByMovimento(Movimento movimento);
}
