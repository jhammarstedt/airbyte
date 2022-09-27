/*
 * Copyright (c) 2022 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.workers.temporal.scheduling.activities;

import io.airbyte.config.persistence.ConfigNotFoundException;
import io.airbyte.validation.json.JsonValidationException;
import io.airbyte.workers.helper.ConnectionHelper;
import io.airbyte.workers.temporal.exception.RetryableException;
import io.micronaut.context.annotation.Requires;
import java.io.IOException;
import javax.inject.Singleton;

@Singleton
@Requires(property = "airbyte.worker.plane",
          pattern = "(?i)^(?!data_plane).*")
public class ConnectionDeletionActivityImpl implements ConnectionDeletionActivity {

  private final ConnectionHelper connectionHelper;

  public ConnectionDeletionActivityImpl(final ConnectionHelper connectionHelper) {
    this.connectionHelper = connectionHelper;
  }

  @Override
  public void deleteConnection(final ConnectionDeletionInput input) {
    try {
      connectionHelper.deleteConnection(input.getConnectionId());
    } catch (final JsonValidationException | ConfigNotFoundException | IOException e) {
      throw new RetryableException(e);
    }
  }

}
